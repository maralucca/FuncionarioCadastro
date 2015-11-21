// DatabaseConnector.java
// Provides easy connection and creation of UserFuncionarios database.
package com.example.funcionariocadastro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnector 
{
   // database nome
   private static final String DATABASE_NAME = "UserFuncionario";
      
   private SQLiteDatabase database; // for interacting with the database
   private DatabaseOpenHelper databaseOpenHelper; // creates the database

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   }

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   }

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } 

   // inserts a new funcionario in the database
   public long insertFuncionario(String nome, String telefone, String email,  
      String endereco, String cargo, String admissao, String salario) 
   {
      ContentValues newFuncionario = new ContentValues();
      newFuncionario.put("nome", nome);
      newFuncionario.put("telefone", telefone);
      newFuncionario.put("email", email);
      newFuncionario.put("endereco", endereco);
      newFuncionario.put("cargo", cargo);
      newFuncionario.put("admissao", admissao);
      newFuncionario.put("salario", salario);

      open(); // open the database
      long rowID = database.insert("funcionarios", null, newFuncionario);
      close(); // close the database
      return rowID;
   } 

   // updates an existing funcionario in the database
   public void updateFuncionario(long id, String nome, String telefone, 
      String email, String endereco, String cargo, String admissao, String salario) 
   {
      ContentValues editFuncionario = new ContentValues();
      editFuncionario.put("nome", nome);
      editFuncionario.put("telefone", telefone);
      editFuncionario.put("email", email);
      editFuncionario.put("endereco", endereco);
      editFuncionario.put("cargo", cargo);
      editFuncionario.put("admissao", admissao);
      editFuncionario.put("salario", salario);

      open(); // open the database
      database.update("funcionarios", editFuncionario, "_id=" + id, null);
      close(); // close the database
   } // end method updateFuncionario

   // return a Cursor with all funcionario nomes in the database
   public Cursor getAllFuncionarios() 
   {
      return database.query("funcionarios", new String[] {"_id", "nome"}, 
         null, null, null, null, "nome");
   } 

   // return a Cursor containing specified funcionario's information 
   public Cursor getOneFuncionario(long id) 
   {
      return database.query(
         "funcionarios", null, "_id=" + id, null, null, null, null);
   } 

   // delete the funcionario specified by the given String nome
   public void deleteFuncionario(long id) 
   {
      open(); // open the database
      database.delete("funcionarios", "_id=" + id, null);
      close(); // close the database
   } 
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      // constructor
      public DatabaseOpenHelper(Context context, String nome,
         CursorFactory factory, int version) 
      {
         super(context, nome, factory, version);
      }

      // creates the funcionarios table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table nomed funcionarios
         String createQuery = "CREATE TABLE funcionarios" +
            "(_id integer primary key autoincrement," +
            "nome TEXT, telefone TEXT, email TEXT, " +
            "endereco TEXT, cargo TEXT, admissao TEXT, salario TEXT);";
                  
         db.execSQL(createQuery); // execute query to create the database
      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      }
   } // end class DatabaseOpenHelper
} // end class DatabaseConnector



