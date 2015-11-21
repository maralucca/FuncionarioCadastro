package com.example.funcionariocadastro;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import android.os.Bundle;


public class SobreActivity extends DialogFragment {
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		

		DialogInterface.OnClickListener fecharClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Aplicativo criado por Ciomara de Lucca RA 20471257");
		builder.setNegativeButton("Fechar a janela", fecharClick);
		Dialog dialog = builder.create();
		return dialog;
	}
}
