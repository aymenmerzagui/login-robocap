
package com.are.roboccap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity  {
    EditText adresse ,password ;
    private Button login ;
    boolean valid;

    {
        valid = true;
    }

    FirebaseAuth fAuth ;
    FirebaseFirestore fStore ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        adresse=findViewById(R.id.adresse);
        password=findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (seconnecter()) {
                        fAuth.signInWithEmailAndPassword(adresse.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                                checkcategory(authResult.getUser().getUid());

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            });


    }


    private boolean seconnecter(){

    String email=adresse.getText().toString().trim();
    String paswd=password.getText().toString().trim();

        if (paswd.isEmpty()){
        //password.setError("password required");
        //password.requestFocus();
        valid=false ;
    }
        if (email.isEmpty()){
        //adresse.setError("username required");
        //adresse.requestFocus();
        valid=false;
    }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        //adresse.setError("please provide valid email");
        //adresse.requestFocus();
        valid=false ;
    }
        if(paswd.length()<6){
        //password.setError("min 6 character");
        //password.requestFocus();
        valid=false ;
    }
        return valid ;
    }
    private  void checkcategory(String uid){
        DocumentReference df =fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: "+documentSnapshot.getData());
                switch (documentSnapshot.getString("category")){
                    case "1" :
                        startActivity(new Intent(getApplicationContext(),admin_activity.class));
                        finish();
                    case "2" :
                        startActivity(new Intent(getApplicationContext(),jury_activity.class));
                        finish();
                    case "3" :
                        startActivity(new Intent(getApplicationContext(),reception_activity.class));
                        finish();
                    case "4" :
                        startActivity(new Intent(getApplicationContext(),homologation_activity.class));
                        finish();

                }

            }
        });


}


}

