package co.realinventor.tossacoin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import java.util.Random;

public class MainActivity extends Activity {
    private MaterialButton tossButton;
    private ImageView coinImageView;
    private String TOSS;
    private final String TAILS = "TAILS";
    private final String HEADS = "HEADS";
    private Runnable updater;
    private int[] resMap = new int[20];
    private int count;
    private Handler timerHandler;
    private int countLoop;
    private TextView tossTextHead;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-4525583199746587~1171843920");

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //Test ad unit
        mInterstitialAd.setAdUnitId("ca-app-pub-4525583199746587/3028206297");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        tossButton = findViewById(R.id.tossButton);
        coinImageView = findViewById(R.id.coinImageView);
        tossTextHead = findViewById(R.id.tossTextHead);

        initialiseRes();


        flipTheCoin();

        tossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipTheCoin();
            }
        });

    }

    private void flipTheCoin(){
        TOSS = tossCoin();
        Log.d("Toss",TOSS);
        tossButton.setVisibility(View.INVISIBLE);
        tossTextHead.setText(R.string.tossing);
        animateView();

    }



    private void animateView(){
        timerHandler = new Handler();

        count = 0;
        countLoop = 0;


        updater = new Runnable() {
            @Override
            public void run() {
                coinImageView.setImageResource(resMap[count]);
                timerHandler.postDelayed(updater,40);

                count++;
                if (count >= 18) {
                    count = 0;
                    countLoop++;
                }

                if(countLoop >= 3){
                    if(TOSS.equals(HEADS)){
                        tossButton.setVisibility(View.VISIBLE);
                        tossTextHead.setText(R.string.heads);

                        //stop at heads
                        if(resMap[count] == R.drawable.coin_2){
                            timerHandler.removeCallbacks(updater);
                        }
                    }
                    else{
                        tossButton.setVisibility(View.VISIBLE);
                        tossTextHead.setText(R.string.tails);
                        //stop at tails
                        if(resMap[count] == R.drawable.coin_11){
                            timerHandler.removeCallbacks(updater);
                        }

                    }
                }
            }
        };
        timerHandler.post(updater);


    }

    private String tossCoin(){
        if(random() == 1){
            return HEADS;
        }
        else{
            return TAILS;
        }
    }

    private int random(){
        return  new Random().nextInt(2) + 1;
    }

    private void initialiseRes(){
        resMap[0] = R.drawable.coin_1;
        resMap[1] = R.drawable.coin_2;
        resMap[2] = R.drawable.coin_3;
        resMap[3] = R.drawable.coin_4;
        resMap[4] = R.drawable.coin_5;
        resMap[5] = R.drawable.coin_6;
        resMap[6] = R.drawable.coin_7;
        resMap[7] = R.drawable.coin_8;
        resMap[8] = R.drawable.coin_9;
        resMap[9] = R.drawable.coin_10;
        resMap[10] = R.drawable.coin_11;
        resMap[11] = R.drawable.coin_12;
        resMap[12] = R.drawable.coin_13;
        resMap[13] = R.drawable.coin_14;
        resMap[14] = R.drawable.coin_15;
        resMap[15] = R.drawable.coin_16;
        resMap[16] = R.drawable.coin_17;
        resMap[17] = R.drawable.coin_18;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(updater);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }


    public void privacyButtonClicked(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://realinventor.github.io/Tossly/privacy.html"));
        startActivity(browserIntent);
    }
}
