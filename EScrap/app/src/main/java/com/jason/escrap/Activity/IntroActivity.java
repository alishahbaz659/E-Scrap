package com.jason.escrap.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.jason.escrap.Fragments.AuthenticationFragment;
import com.jason.escrap.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        editor = getSharedPreferences("state", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("state", MODE_PRIVATE);


        if (!sharedPreferences.contains("firstrun")) {
            PaperOnboardingPage scr1 = new PaperOnboardingPage("Find A Scraper Near You",
                    "Get in touch with scrapers near you",
                    Color.parseColor("#FFFFFF"), R.drawable.recycle1, R.drawable.onboarding_pager_circle_icon);
            PaperOnboardingPage scr2 = new PaperOnboardingPage("Find A Customers For Your Products",
                    "You can find different customers for your products",
                    Color.parseColor("#FFFFFF"), R.drawable.recycle2, R.drawable.onboarding_pager_circle_icon);
            PaperOnboardingPage scr3 = new PaperOnboardingPage("Find A Seller Near You",
                    "Find products for your home appliances",
                    Color.parseColor("#FFFFFF"), R.drawable.recycle3, R.drawable.onboarding_pager_circle_icon);

            ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
            elements.add(scr1);
            elements.add(scr2);
            elements.add(scr3);

            PaperOnboardingFragment onboardingFragment = PaperOnboardingFragment.newInstance(elements);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.intro_activity_container, onboardingFragment);
            fragmentTransaction.commit();

            onboardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
                @Override
                public void onRightOut() {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Fragment bf = new AuthenticationFragment();
                    fragmentTransaction.replace(R.id.intro_activity_container, bf);
                    fragmentTransaction.commit();
                }
            });

            editor.putString("firstrun", "done");
            editor.commit();
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment bf = new AuthenticationFragment();
            fragmentTransaction.replace(R.id.intro_activity_container, bf);
            fragmentTransaction.commit();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}