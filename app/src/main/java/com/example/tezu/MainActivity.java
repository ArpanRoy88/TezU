package com.example.tezu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tezu.Adapter.MyBuySellRecyclerAdapter;
import com.example.tezu.Fragment.BuySellFragment;
import com.example.tezu.Fragment.EventFragment;
import com.example.tezu.Fragment.ForumFragment;
import com.example.tezu.Fragment.MagazineFragment;
import com.example.tezu.Fragment.MyBuySellFragment;
import com.example.tezu.Fragment.MyEventFragment;
import com.example.tezu.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    BottomSheetDialog bottomSheet;

    //NAVIGATION DRAWER variable(START)
    CircleImageView navHeaderProfileIcon;
    Toolbar myToolbar;

    String email,email2,email3;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private TextView txtLoggedInAs;
    private TextView txtNavHeaderEmail,txtNavHeaderUserName;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //END
    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //FOR COMMENT(START)
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE)
                    .edit();
            editor.putString("profileid", publisher);
            editor.apply();

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new EventFragment()).commit();
        //FOR COMMENT(END)

        //DRAWER NAVIGATION(START)
        //init
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Student");

        navigationView = findViewById(R.id.myNavigationDrawer);
        drawerLayout = findViewById(R.id.drawer);


        //adding toolbar
        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("TezU");
        setSupportActionBar(myToolbar);

        //setting the toggle
        toggle = new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sendEmail();

        //navigation onclick listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.myEvents:
                        selectedFragment = new MyEventFragment();
                        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
                        fragmentManager.replace(R.id.fragment_container, selectedFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.myAds:

                        selectedFragment = new MyBuySellFragment();
                        fragmentManager = getSupportFragmentManager().beginTransaction();
                        fragmentManager.replace(R.id.fragment_container, selectedFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.TUNotifications:

                        Toast.makeText(getApplicationContext(),"Notifications",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.TUWebsite:

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tezu.ernet.in/"));
                        startActivity(browserIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.AboutUs:

                        Intent aboutIntent = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(aboutIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.EmergencyContacts:

                        Intent contactIntent = new Intent(MainActivity.this, Contacts.class);
                        startActivity(contactIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.Logout:

                        logout();
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });



    }
    //DRAWER NAVIGATION(END)


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                    switch (menuItem.getItemId()){
                        case R.id.nav_event :
                            selectedFragment = new EventFragment();
                            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
                            fragmentManager.replace(R.id.fragment_container, selectedFragment).commit();
                            return true;


                        case R.id.nav_magazine :
                            selectedFragment = new MagazineFragment();
                            fragmentManager = getSupportFragmentManager().beginTransaction();
                            fragmentManager.replace(R.id.fragment_container, selectedFragment).commit();
                            return true;

                        case R.id.nav_add :
                            bottomSheet = new BottomSheetDialog();
                            bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
                            return true;
//                            selectedFragment = null;
//                            startActivity(new Intent( MainActivity.this, PostActivity.class));
//                            return true;

                        case R.id.nav_forum :
                            selectedFragment = new ForumFragment();
                            fragmentManager = getSupportFragmentManager().beginTransaction();
                            fragmentManager.replace(R.id.fragment_container, selectedFragment).commit();
                            return true;


                        case R.id.nav_bus :
                            selectedFragment = new BuySellFragment();
                            fragmentManager = getSupportFragmentManager().beginTransaction();
                            fragmentManager.replace(R.id.fragment_container, selectedFragment).commit();
                            return true;
                    }

                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }
                    return true;
                }

            };


    protected void sendEmail(){
        Intent getIntent = getIntent();
        email = getIntent.getStringExtra("Email");

        email3 = email;

    }


    private void checkUserStatus(){
        //get current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            //user is signed in

            sendEmail();
            updateHeader();
        }
        else {
            //user not signed in
            startActivity(new Intent(MainActivity.this,MainLogin.class));
            finish();
        }
    }


    //logout function
    public void logout() {

        FirebaseAuth.getInstance().signOut();
        Intent gotologin = new Intent(getApplicationContext(),MainLogin.class);
        gotologin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotologin);

        finish();
    }

    // navigation drawer header update
    private void updateHeader(){

        navigationView = findViewById(R.id.myNavigationDrawer);
        View headerView = navigationView.getHeaderView(0);

        txtNavHeaderUserName = headerView.findViewById(R.id.drawerHeaderUserName);
        txtNavHeaderEmail = headerView.findViewById(R.id.drawerHeaderUserEmail);
        navHeaderProfileIcon = headerView.findViewById(R.id.drawerHeaderProfilePic);

        fetchData(navHeaderProfileIcon);
        txtNavHeaderEmail.setText(firebaseUser.getEmail());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("Email").getValue().equals(firebaseUser.getEmail())) {

                        txtNavHeaderUserName.setText(ds.child("StudentName").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navHeaderProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"CLICKED", Toast.LENGTH_SHORT).show();

                Intent profileIcon = new Intent(MainActivity.this,UserProfile.class);

                email2 = txtNavHeaderEmail.getText().toString();
                profileIcon.putExtra("Email",email2);

                startActivity(profileIcon);
            }
        });

    }

    private void fetchData(final ImageView studentProfilePicture){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getProfilePic().equals("Null"))
                {
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tezu-33542." +
                            "appspot.com/o/Student%2Fuserphoto.png?alt=media&token=db4d39c7-e8a5-" +
                            "47fe-8624-c40a4d22a626").into(studentProfilePicture);
                }
                else {
                    Picasso.get().load(user.getProfilePic()).into(studentProfilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
