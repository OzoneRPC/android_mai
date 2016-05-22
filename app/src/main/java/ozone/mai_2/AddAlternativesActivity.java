package ozone.mai_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ozone on 03.05.2016.
 */
public class AddAlternativesActivity extends AppCompatActivity {
    private List<View> textfields;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alternatives);
        this.textfields = new ArrayList<View>();
        Button add_alternative_button = (Button)findViewById(R.id.add_alternative_button);
        final LinearLayout add_alternative_linear = (LinearLayout)findViewById(R.id.add_alternative_linear);

        add_alternative_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
                add_alternative_linear.addView(view);
            }
        });
    }
}
