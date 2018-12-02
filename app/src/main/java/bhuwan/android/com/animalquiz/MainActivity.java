package bhuwan.android.com.animalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {


    public static final String GUESSES="setting_numberOfGuesses";
    public static final String ANIMALS_TYPE="setting_animalsType";
    public static final String QUIZ_BACKGROUND_COLOR="setting_quiz_background_color";
    public static final String QUIZ_FONT="setting_quiz_font";

    private boolean isSettingChanged=false;

    static Typeface chunkfive;
    static Typeface fontleroyBrown;
    static Typeface wonderbarDemo;

    MainActivityFragment myAnimalQuizFragment;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chunkfive= Typeface.createFromAsset(getAssets(),"fonts/Chunkfive.otf");
        fontleroyBrown=Typeface.createFromAsset(getAssets(),"fonts/FontleroyBrown.ttf");
        wonderbarDemo=Typeface.createFromAsset(getAssets(),"fonts/Wonderbar Demo.otf");

        PreferenceManager.setDefaultValues(MainActivity.this,R.xml.quiz_preferences,false);
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
          registerOnSharedPreferenceChangeListener(settingsChangeListener);

        myAnimalQuizFragment=(MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);

      myAnimalQuizFragment.modifyAnimalsGuessRows(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
      myAnimalQuizFragment.modifyTypeOfAnimalsQuiz(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
      myAnimalQuizFragment.modifyQuizFont(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
      myAnimalQuizFragment.modifyBackgroundColor(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
      myAnimalQuizFragment.resetAnimalQuiz();
      isSettingChanged=false;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent preferenceIntent=new Intent(MainActivity.this,SettingsActivity.class);
        startAction(preferenceIntent);
        return super.onOptionsItemSelected(item);
        


    }

    private void startAction(Intent preferenceIntent) {
    }

    private SharedPreferences.OnSharedPreferenceChangeListener settingsChangeListener
            =new SharedPreferences.OnSharedPreferenceChangeListener(){

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

             isSettingChanged=true;
             if (key.equals(GUESSES)){

                 myAnimalQuizFragment.modifyAnimalsGuessRows(sharedPreferences);
                 myAnimalQuizFragment.resetAnimalQuiz();
             } else if (key.equals(ANIMALS_TYPE)){
                 Set<String>animalTypes =sharedPreferences.getStringSet(ANIMALS_TYPE, null);

                 if (animalTypes!=null && animalTypes.size()>0){
                     myAnimalQuizFragment.modifyTypeOfAnimalsQuiz(sharedPreferences);
                     myAnimalQuizFragment.resetAnimalQuiz();
                 } else{
                     SharedPreferences.Editor editor=sharedPreferences.edit();
                     animalTypes.add(getString(R.string.default_animal_type));
                     editor.putStringSet(ANIMALS_TYPE,animalTypes);
                     editor.apply();

                     Toast.makeText(MainActivity.this,
                             R.string.toast_message,Toast.LENGTH_SHORT).show();


                 }
             } else if(key.equals(QUIZ_FONT)){

                 myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                 myAnimalQuizFragment.resetAnimalQuiz();
             }

             Toast.makeText(MainActivity.this,R.string.change_message,Toast.LENGTH_SHORT).show();





        }
    };

}
