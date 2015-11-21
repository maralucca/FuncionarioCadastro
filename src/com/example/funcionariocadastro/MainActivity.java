package com.example.funcionariocadastro;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity implements ListaFuncionario.FuncionarioListFragmentListener,
		Detalhes.DetalhesFragmentListener, AdicionarEditar.AdicionarEditartFragmentListener {

	public static final String ROW_ID = "row_id";

	ListaFuncionario funcionarioListFragment;

	// mostra a lista de funcionários quando carrega
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null)
			return;

		if (findViewById(R.id.fragmentContainer) != null) {

			funcionarioListFragment = new ListaFuncionario();

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.add(R.id.fragmentContainer, funcionarioListFragment);
			transaction.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (funcionarioListFragment == null) {
			funcionarioListFragment = (ListaFuncionario) getFragmentManager()
					.findFragmentById(R.id.funcionarioListFragment);
		}
	}

	@Override
	public void onFuncionarioSelected(long rowID) {
		if (findViewById(R.id.fragmentContainer) != null)
			displayFuncionario(rowID, R.id.fragmentContainer);
		else {
			getFragmentManager().popBackStack();
			displayFuncionario(rowID, R.id.rightPaneContainer);
		}
	}

	private void displayFuncionario(long rowID, int viewID) {
		Detalhes detailsFragment = new Detalhes();

		Bundle arguments = new Bundle();
		arguments.putLong(ROW_ID, rowID);
		detailsFragment.setArguments(arguments);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(viewID, detailsFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onAddFuncionario() {
		if (findViewById(R.id.fragmentContainer) != null)
			displayAddEditFragment(R.id.fragmentContainer, null);
		else
			displayAddEditFragment(R.id.rightPaneContainer, null);
	}

	private void displayAddEditFragment(int viewID, Bundle arguments) {
		AdicionarEditar addEditFragment = new AdicionarEditar();

		if (arguments != null)
			addEditFragment.setArguments(arguments);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(viewID, addEditFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onFuncionarioDeleted() {
		getFragmentManager().popBackStack();

		if (findViewById(R.id.fragmentContainer) == null)
			funcionarioListFragment.updateFuncionarioList();
	}

	@Override
	public void onEditFuncionario(Bundle arguments) {
		if (findViewById(R.id.fragmentContainer) != null)
			displayAddEditFragment(R.id.fragmentContainer, arguments);
		else // tablet
			displayAddEditFragment(R.id.rightPaneContainer, arguments);
	}

	@Override
	public void onAdicionarEditarCompleted(long rowID) {
		getFragmentManager().popBackStack();

		if (findViewById(R.id.fragmentContainer) == null) {
			getFragmentManager().popBackStack();
			funcionarioListFragment.updateFuncionarioList();

			displayFuncionario(rowID, R.id.rightPaneContainer);
		}
	}

	@Override
	public void openDialogFragment() {
		FragmentManager fm = getFragmentManager();
		SobreActivity SobreActivity = new SobreActivity();

		SobreActivity.show(fm, "choose from here");

	}

}
