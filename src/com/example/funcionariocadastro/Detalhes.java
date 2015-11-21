package com.example.funcionariocadastro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Detalhes extends Fragment {

	public interface DetalhesFragmentListener {

		public void onFuncionarioDeleted();

		public void onEditFuncionario(Bundle arguments);
	}

	private DetalhesFragmentListener listener;

	private long rowID = -1;
	private TextView nomeTextView;
	private TextView telefoneTextView;
	private TextView emailTextView;
	private TextView enderecoTextView;
	private TextView cargoTextView;
	private TextView admissaoTextView;
	private TextView salarioTextView;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (DetalhesFragmentListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setRetainInstance(true);

		if (savedInstanceState != null)
			rowID = savedInstanceState.getLong(MainActivity.ROW_ID);
		else {

			Bundle arguments = getArguments();

			if (arguments != null)
				rowID = arguments.getLong(MainActivity.ROW_ID);
		}

		View view = inflater.inflate(R.layout.activity_detlahes, container, false);
		setHasOptionsMenu(true);

		nomeTextView = (TextView) view.findViewById(R.id.nomeTextView);
		telefoneTextView = (TextView) view.findViewById(R.id.telefoneTextView);
		emailTextView = (TextView) view.findViewById(R.id.emailTextView);
		enderecoTextView = (TextView) view.findViewById(R.id.enderecoTextView);
		cargoTextView = (TextView) view.findViewById(R.id.cargoTextView);
		admissaoTextView = (TextView) view.findViewById(R.id.admissaoTextView);
		salarioTextView = (TextView) view.findViewById(R.id.salarioTextView);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		new LoadFuncionarioTask().execute(rowID);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(MainActivity.ROW_ID, rowID);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_detalhes_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:

			Bundle arguments = new Bundle();
			arguments.putLong(MainActivity.ROW_ID, rowID);
			arguments.putCharSequence("nome", nomeTextView.getText());
			arguments.putCharSequence("telefone", telefoneTextView.getText());
			arguments.putCharSequence("email", emailTextView.getText());
			arguments.putCharSequence("endereco", enderecoTextView.getText());
			arguments.putCharSequence("cargo", cargoTextView.getText());
			arguments.putCharSequence("admissao", admissaoTextView.getText());
			arguments.putCharSequence("salario", salarioTextView.getText());
			listener.onEditFuncionario(arguments);
			return true;
		case R.id.action_delete:
			deleteFuncionario();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class LoadFuncionarioTask extends AsyncTask<Long, Object, Cursor> {
		DatabaseConnector databaseConnector = new DatabaseConnector(getActivity());

		@Override
		protected Cursor doInBackground(Long... params) {
			databaseConnector.open();
			return databaseConnector.getOneFuncionario(params[0]);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			result.moveToFirst(); // move to the first item

			int nomeIndex = result.getColumnIndex("nome");
			int telefoneIndex = result.getColumnIndex("telefone");
			int emailIndex = result.getColumnIndex("email");
			int enderecoIndex = result.getColumnIndex("endereco");
			int cargoIndex = result.getColumnIndex("cargo");
			int admissaoIndex = result.getColumnIndex("admissao");
			int salarioIndex = result.getColumnIndex("salario");

			nomeTextView.setText(result.getString(nomeIndex));
			telefoneTextView.setText(result.getString(telefoneIndex));
			emailTextView.setText(result.getString(emailIndex));
			enderecoTextView.setText(result.getString(enderecoIndex));
			cargoTextView.setText(result.getString(cargoIndex));
			admissaoTextView.setText(result.getString(admissaoIndex));
			salarioTextView.setText(result.getString(salarioIndex));

			result.close();
			databaseConnector.close();
		}
	}

	private void deleteFuncionario() {

		confirmDelete.show(getFragmentManager(), "confirm delete");
	}

	private DialogFragment confirmDelete = new DialogFragment() {

		@Override
		public Dialog onCreateDialog(Bundle bundle) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle(R.string.confirm_title);
			builder.setMessage(R.string.confirm_message);

			builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {
					final DatabaseConnector databaseConnector = new DatabaseConnector(getActivity());

					AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
						@Override
						protected Object doInBackground(Long... params) {
							databaseConnector.deleteFuncionario(params[0]);
							return null;
						}

						@Override
						protected void onPostExecute(Object result) {
							listener.onFuncionarioDeleted();
						}
					};

					deleteTask.execute(new Long[] { rowID });
				}
			});

			builder.setNegativeButton(R.string.button_cancel, null);
			return builder.create();
		}
	};
}
