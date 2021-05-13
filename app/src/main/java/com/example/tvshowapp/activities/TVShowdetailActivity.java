package com.example.tvshowapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tvshowapp.R;
import com.example.tvshowapp.adapters.EpisodeAdapter;
import com.example.tvshowapp.adapters.ImageSliderAdapter;
import com.example.tvshowapp.databinding.ActivityTvshowdetailBinding;
import com.example.tvshowapp.databinding.LayoutEpisodeButtonSheetBinding;
import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowdetailActivity extends AppCompatActivity {

    private ActivityTvshowdetailBinding activityTvshowdetailBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodeButtonSheetBinding layoutEpisodeButtonSheetBinding;

    private TVShow tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshowdetail);
        activityTvshowdetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshowdetail);

        doInitialization();

    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowdetailBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        getTVShowDetails();
    }


    private void getTVShowDetails() {
        activityTvshowdetailBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            activityTvshowdetailBinding.setIsLoading(false);
            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {

                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }

                activityTvshowdetailBinding.setTvShowImageURL(
                        tvShowDetailsResponse.getTvShowDetails().getImagePath()
                );
                activityTvshowdetailBinding.imageTVShow.setVisibility(View.VISIBLE);
                activityTvshowdetailBinding.setDescription(
                        String.valueOf(
                                HtmlCompat.fromHtml(
                                        tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                        )
                );
                activityTvshowdetailBinding.textDescription.setVisibility(View.VISIBLE);
                activityTvshowdetailBinding.textReadMore.setVisibility(View.VISIBLE);
                activityTvshowdetailBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityTvshowdetailBinding.textReadMore.getText().toString().equals("Read More")) {
                            activityTvshowdetailBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                            activityTvshowdetailBinding.textDescription.setEllipsize(null);
                            activityTvshowdetailBinding.textReadMore.setText("Read less");
                        } else {
                            activityTvshowdetailBinding.textDescription.setMaxLines(4);
                            activityTvshowdetailBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                            activityTvshowdetailBinding.textReadMore.setText(R.string.read_more_text);
                        }
                    }
                });
                activityTvshowdetailBinding.setRating(
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                        )
                );
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    activityTvshowdetailBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    activityTvshowdetailBinding.setGenre("N/A");
                }
                activityTvshowdetailBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                activityTvshowdetailBinding.viewDivider1.setVisibility(View.VISIBLE);
                activityTvshowdetailBinding.viewDivider2.setVisibility(View.VISIBLE);
                activityTvshowdetailBinding.layoutMisc.setVisibility(View.VISIBLE);

                activityTvshowdetailBinding.buttonWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                        startActivity(intent);
                    }
                });
                activityTvshowdetailBinding.buttonWebsite.setVisibility(View.VISIBLE);

                activityTvshowdetailBinding.buttonEpisode.setVisibility(View.VISIBLE);

                activityTvshowdetailBinding.buttonEpisode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (episodeBottomSheetDialog == null) {
                            episodeBottomSheetDialog = new BottomSheetDialog(TVShowdetailActivity.this);
                            layoutEpisodeButtonSheetBinding = DataBindingUtil.inflate(
                                    LayoutInflater.from(TVShowdetailActivity.this),
                                    R.layout.layout_episode_button_sheet,
                                    findViewById(R.id.episodesContainer),
                                    false
                            );
                            episodeBottomSheetDialog.setContentView(layoutEpisodeButtonSheetBinding.getRoot());
                            layoutEpisodeButtonSheetBinding.episodeRecyclerView.setAdapter(
                                    new EpisodeAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                            );
                            layoutEpisodeButtonSheetBinding.textTitle.setText(
                                    String.format("Episode | %s", tvShow.getName())
                            );
                            layoutEpisodeButtonSheetBinding.imageClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    episodeBottomSheetDialog.dismiss();
                                }
                            });
                        }

                        // ------ Optional section start ---- //
                        FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                                com.google.android.material.R.id.design_bottom_sheet
                        );
                        if (frameLayout != null) {
                            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        //----- Optional section end --- //
                        episodeBottomSheetDialog.show();
                    }
                });

               /* activityTvshowdetailBinding.imageWatchList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CompositeDisposable().add(tvShowDetailsViewModel.addToWatchList(tvShow)
                        .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())

                        )
                    }
                });

                */
                activityTvshowdetailBinding.imageWatchList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });
    }

    private void loadImageSlider(String[] sliderImage) {
        activityTvshowdetailBinding.slideerViewPager.setOffscreenPageLimit(1);
        activityTvshowdetailBinding.slideerViewPager.setAdapter(new ImageSliderAdapter(sliderImage));
        activityTvshowdetailBinding.slideerViewPager.setVisibility(View.VISIBLE);
        activityTvshowdetailBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImage.length);
        activityTvshowdetailBinding.slideerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setcurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowdetailBinding.layoutSliderIndicator.addView(indicators[i]);
        }
        activityTvshowdetailBinding.layoutSliderIndicator.setVisibility(View.VISIBLE);
        setcurrentSliderIndicator(0);
    }

    private void setcurrentSliderIndicator(int position) {
        int childCount = activityTvshowdetailBinding.layoutSliderIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowdetailBinding.layoutSliderIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));
            }
        }
    }

    private void loadBasicTVShowDetails() {
        activityTvshowdetailBinding.setTvShowName(tvShow.getName());
        activityTvshowdetailBinding.setNwrworkCountry(
                tvShow.getNetwork() + "(" + tvShow.getCountry() + ")"
        );
        activityTvshowdetailBinding.setStatus(tvShow.getStatus());
        activityTvshowdetailBinding.setStartDate(tvShow.getStartDate());
        activityTvshowdetailBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowdetailBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowdetailBinding.textStarted.setVisibility(View.VISIBLE);
        activityTvshowdetailBinding.textstatus.setVisibility(View.VISIBLE);

    }
}
