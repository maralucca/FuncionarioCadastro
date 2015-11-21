package com.example.funcionariocadastro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AdicionarEditar extends Fragment {

	public interface AdicionarEditartFragmentListener {

		public void onAdicionarEditarCompleted(long rowID);
	}

	private AdicionarEditartFragmentListener listener;

	private long rowID;
	private Bundle funcionarioInfoBundle;

	private EditText nomeEditText;
	private EditText telefoneEditText;
	private EditText emailEditText;
	private EditText enderecoEditText;
	private EditText cargoEditText;
	private EditText admissaoEditText;
	private EditText salarioEditText;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (AdicionarEditartFragmentListener) activity;
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
		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.activity_adicionar_editar, container, false);
		nomeEditText = (EditText) view.findViewById(R.id.nomeEditText);
		telefoneEditText = (EditText) view.findViewById(R.id.telefoneEditText);
		emailEditText = (EditText) view.findViewById(R.id.emailEditText);
		enderecoEditText = (EditText) view.findViewById(R.id.enderecoEditText);
		cargoEditText = (EditText) view.findViewById(R.id.cargoEditText);
		admissaoEditText = (EditText) view.findViewById(R.id.admissaoEditText);
		salarioEditText = (EditText) view.findViewById(R.id.salarioEditText);

		funcionarioInfoBundle = getArguments();

		if (funcionarioInfoBundle != null) {
			rowID = funcionarioInfoBundle.getLong(MainActivity.ROW_ID);
			nomeEditText.setText(funcionarioInfoBundle.getString("nome"));
			telefoneEditText.setText(funcionarioInfoBundle.getString("telefone"));
			emailEditText.setText(funcionarioInfoBundle.getString("email"));
			enderecoEditText.setText(funcionarioInfoBundle.getString("endereco"));
			cargoEditText.setText(funcionarioInfoBundle.getString("cargo"));
			admissaoEditText.setText(funcionarioInfoBundle.getString("admissao"));
			salarioEditText.setText(funcionarioInfoBundle.getString("salario"));
		}

		Button saveFuncionarioButton = (Button) view.findViewById(R.id.saveFuncionariotButton);
		saveFuncionarioButton.setOnClickListener(saveFuncionarioButtonClicked);
		return view;
	}

	OnClickListener saveFuncionarioButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (nomeEditText.getText().toString().trim().length() != 0) {

				AsyncTask<Object, Object, Object> saveFuncionarioTask = new AsyncTask<Object, Object, Object>() {
					@Override
					protected Object doInBackground(Object... params) {
						saveFuncionario();
						return null;
					}

					@Override
					protected void onPostExecute(Object result) {
						// hide soft keyboard
						InputMethodManager imm = (InputMethodManager) getActivity()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

						listener.onAdicionarEditarCompleted(rowID);
					}
				};

				saveFuncionarioTask.execute((Object[]) null);
			} else {
				DialogFragment errorSaving = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setMessage(R.string.error_message);
						builder.setPositiveButton(R.string.ok, null);
						return builder.create();
					}
				};

				errorSaving.show(getFragmentManager(), "erro salavando funcionario");
			}
		}
	};

	private void saveFuncionario() {

		DatabaseConnector databaseConnector = new DatabaseConnector(getActivity());

		if (funcionarioInfoBundle == null) {

			rowID = databaseConnector.insertFuncionario(nomeEditText.getText().toString(),
					telefoneEditText.getText().toString(), emailEditText.getText().toString(),
					enderecoEditText.getText().toString(), cargoEditText.getText().toString(),
					admissaoEditText.getText().toString(), salarioEditText.getText().toString());
		} else {
			databaseConnector.updateFuncionario(rowID, nomeEditText.getText().toString(),
					telefoneEditText.getText().toString(), emailEditText.getText().toString(),
					enderecoEditText.getText().toString(), cargoEditText.getText().toString(),
					admissaoEditText.getText().toString(), salarioEditText.getText().toString());
		}
	}
}
