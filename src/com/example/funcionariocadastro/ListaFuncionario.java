package com.example.funcionariocadastro;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListaFuncionario extends ListFragment {

	public interface FuncionarioListFragmentListener {

		public void onFuncionarioSelected(long rowID);

		public void onAddFuncionario();

		public void openDialogFragment();
	}

	private FuncionarioListFragmentListener listener;

	private ListView funcionarioListView;
	private CursorAdapter funcionarioAdapter;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FuncionarioListFragmentListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);

		setEmptyText(getResources().getString(R.string.no_records));

		funcionarioListView = getListView();
		funcionarioListView.setOnItemClickListener(viewFuncionarioListener);
		funcionarioListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		String[] from = new String[] { "nome" };
		int[] to = new int[] { android.R.id.text1 };
		funcionarioAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to,
				0);
		setListAdapter(funcionarioAdapter);
	}

	OnItemClickListener viewFuncionarioListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			listener.onFuncionarioSelected(id);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		new GetFuncionariosTask().execute((Object[]) null);
	}

	private class GetFuncionariosTask extends AsyncTask<Object, Object, Cursor> {
		DatabaseConnector databaseConnector = new DatabaseConnector(getActivity());

		@Override
		protected Cursor doInBackground(Object... params) {
			databaseConnector.open();
			return databaseConnector.getAllFuncionarios();
		}

		@Override
		protected void onPostExecute(Cursor result) {
			funcionarioAdapter.changeCursor(result);
			databaseConnector.close();
		}
	}

	@Override
	public void onStop() {
		Cursor cursor = funcionarioAdapter.getCursor();
		funcionarioAdapter.changeCursor(null);

		if (cursor != null)
			cursor.close();

		super.onStop();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_funcionario_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			listener.onAddFuncionario();
			return true;
		case R.id.action_settings:
			listener.openDialogFragment();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void updateFuncionarioList() {
		new GetFuncionariosTask().execute((Object[]) null);
	}

}
