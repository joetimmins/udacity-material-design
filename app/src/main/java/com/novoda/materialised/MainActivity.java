package com.novoda.materialised;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.novoda.materialised.databinding.MainActivityBinding;
import com.novoda.materialised.example.MessagePresenter;
import com.novoda.materialised.example.ToggleMessages;
import com.novoda.materialised.firebase.FirebaseItems;
import com.novoda.materialised.firebase.FirebaseSingleton;
import com.novoda.materialised.firebase.FirebaseTopStories;
import com.novoda.materialised.hackernews.StoryViewModel;
import com.novoda.materialised.hackernews.database.TopStories;
import com.novoda.materialised.hackernews.database.ValueCallback;

import java.util.Arrays;
import java.util.List;

public final class MainActivity extends AppCompatActivity {

    private MainActivityBinding mainActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityLayout = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(mainActivityLayout.toolbar);

        mainActivityLayout.fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Toast.makeText(MainActivity.this, "This button does nothing", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        final MessagePresenter messagePresenter = new MessagePresenter(new ToggleMessages("hello world!", "hello again!"));

        mainActivityLayout.helloWorldButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            int finalRadius = (int) Math.hypot(view.getWidth() / 2, view.getHeight() / 2);
                            ((Button) view).setText(messagePresenter.currentMessage());
                            ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2, view.getHeight() / 2, 0, finalRadius).start();
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Integer> topStoryIds = Arrays.asList(11846556, 11847529, 11845770, 11847526, 11847625, 11845841, 11847214, 11846353, 11846999, 11847803, 11845804, 11844884, 11846707, 11820512, 11845621, 11845689, 11846887, 11844171, 11847869, 11846642, 11845687, 11847858, 11844152, 11844574, 11847946, 11845698, 11844005, 11845573, 11840342, 11842386, 11845517, 11843878, 11846626, 11843696, 11828406, 11846813, 11843992, 11846722, 11843671, 11846372, 11845582, 11842779, 11846393, 11840453, 11842799, 11840933, 11842176, 11843990, 11843889, 11844788, 11843233, 11845216, 11843214, 11840175, 11846199, 11839603, 11837475, 11839690, 11842084, 11843655, 11837501, 11840479, 11841502, 11843181, 11843347, 11834935, 11842893, 11846423, 11840340, 11843624, 11843477, 11836832, 11840219, 11838017, 11840573, 11842703, 11844678, 11844663, 11840314, 11836290, 11840406, 11843058, 11842169, 11836884, 11839636, 11840931, 11841657, 11837511, 11839560, 11840454, 11837204, 11839943, 11839746, 11833414, 11840455, 11841798, 11837578, 11841544, 11835999, 11837082, 11836523, 11842301, 11829994, 11832656, 11842156, 11841604, 11829713, 11845419, 11836728, 11845741, 11840865, 11817758, 11844214, 11822484, 11839303, 11841330, 11835091, 11834131, 11814828, 11839348, 11829237, 11840020, 11830659, 11831060, 11844151, 11832266, 11823949, 11837674, 11842808, 11823902, 11842749, 11829438, 11828346, 11843804, 11834888, 11827610, 11838490, 11830598, 11845001, 11834492, 11841584, 11837483, 11837941, 11832941, 11828409, 11817863, 11837459, 11820490, 11843604, 11842883, 11843712, 11830329, 11837287, 11843754, 11843506, 11826782, 11827781, 11841019, 11844901, 11839846, 11836584, 11823251, 11819402, 11826186, 11837626, 11815949, 11826031, 11837259, 11815631, 11832056, 11813648, 11833994, 11835720, 11833996, 11827808, 11823691, 11844570, 11836931, 11845051, 11836180, 11819822, 11821295, 11831029, 11812497, 11832349, 11821205, 11821656, 11842635, 11817959, 11840415, 11809846, 11829148, 11824428, 11821200, 11839969, 11821903, 11825827, 11829978, 11843395, 11828732, 11823781, 11831540, 11816122, 11811668, 11832512, 11830413, 11821117, 11812196, 11812794, 11824372, 11831081, 11826175, 11832828, 11827005, 11836905, 11844434, 11818551, 11811927, 11829269, 11833642, 11822562, 11830498, 11814758, 11839705, 11823274, 11812624, 11831935, 11827764, 11837609, 11815417, 11836520, 11817158, 11824203, 11840489, 11814637, 11814531, 11832237, 11810747, 11816211, 11824601, 11837831, 11820417, 11827940, 11838147, 11813038, 11809448, 11819738, 11826216, 11838072, 11816523, 11841694, 11817701, 11836341, 11825364, 11820913, 11840325, 11816527, 11844399, 11837466, 11811387, 11843021, 11815669, 11825554, 11814455, 11820176, 11809911, 11824164, 11811118, 11842791, 11820388, 11841248, 11821229, 11817878, 11842767, 11816852, 11834368, 11822164, 11811618, 11822514, 11813473, 11833009, 11821425, 11819681, 11817917, 11829194, 11812798, 11827899, 11821629, 11837505, 11810815, 11834554, 11819804, 11817114, 11843849, 11842202, 11829373, 11821178, 11812723, 11841476, 11824375, 11818012, 11834210, 11813717, 11821041, 11826690, 11834184, 11812488, 11810594, 11834785, 11830108, 11823647, 11842457, 11816846, 11832966, 11832845, 11836291, 11821028, 11814733, 11816546, 11816992, 11819525, 11829745, 11814962, 11824668, 11840221, 11810754, 11822797, 11814665, 11816559, 11811640, 11813978, 11824635, 11822022, 11810576, 11831845, 11809791, 11810773, 11840985, 11816663, 11816469, 11809581, 11815857, 11811061, 11839684, 11827344, 11810462, 11814126, 11837256, 11813500, 11830793, 11817721, 11824378, 11810802, 11811723, 11809789, 11824593, 11827463, 11811287, 11811049, 11811383, 11825007, 11809591, 11826431, 11809540, 11834319, 11832342, 11818214, 11816772, 11816493, 11829732, 11825948, 11813604, 11840038, 11843273, 11837901, 11823426, 11819043, 11817590, 11810322, 11813988, 11811331, 11813069, 11810876, 11814002, 11830183, 11816752, 11828214, 11836391, 11833155, 11830958, 11828672, 11839476, 11813171, 11810547, 11817226, 11836018, 11832767, 11815124, 11827503, 11826318, 11831088, 11834581, 11825259, 11809754, 11825598, 11833484, 11841206, 11832023, 11827948, 11817902, 11823585, 11811647, 11829152, 11817061, 11816871, 11825815, 11821654, 11833035, 11816216, 11812713, 11838798, 11821697, 11810736, 11814636, 11821150, 11837159, 11837017, 11836891, 11835193, 11839332, 11836754, 11809976, 11809615, 11818618, 11819755, 11818951, 11821256, 11831596, 11818494, 11818472, 11817794, 11835838, 11816474, 11815825, 11815907, 11815428, 11833620, 11822231, 11816224, 11840079, 11814315, 11810266, 11825758, 11823955, 11832719, 11833718, 11814829, 11827243, 11817428, 11828838, 11811775, 11835884, 11811972, 11828558, 11811670, 11811651, 11834778, 11834776, 11821587, 11811201, 11811161, 11817127, 11819736, 11822497, 11817295, 11832597, 11837012, 11825982, 11825857, 11810150, 11821231, 11828665, 11834669, 11811196, 11811308, 11817139, 11812634, 11819955, 11812050, 11826501, 11825084, 11818429, 11811385, 11816067, 11825626, 11824365, 11825388, 11817202, 11816750, 11829104);
        TopStories topStories = new FirebaseTopStories(topStoryIds);
        final FirebaseApp firebaseApp = FirebaseSingleton.INSTANCE.getFirebaseApp(this);
        topStories.readAll(new ValueCallback<List<Integer>>() {
            @Override
            public void onValueRetrieved(List<Integer> value) {
                new FirebaseItems(firebaseApp).readItem(value.get(0), new ValueCallback<StoryViewModel>() {
                    @Override
                    public void onValueRetrieved(StoryViewModel value) {
                        mainActivityLayout.firebaseTextView.setText(value.getTitle());
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
