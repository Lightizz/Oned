package fr.lightiz.oned.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.lightiz.oned.Account;

public class DatabaseManager {
    final String databaseURI = "https://oned-3f754-default-rtdb.firebaseio.com/";
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("accounts");
    List<Account> accountList = new ArrayList<>();

    public void createNewAccount(Account accountToCreate){
        
    }
}
