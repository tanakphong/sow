package th.co.wesoft.sow;

import android.app.Fragment;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PoterHsu on 3/7/17.
 */

public class MarqueeTextFragment extends Fragment {

    public static final String TAG = MarqueeTextFragment.class.getSimpleName();

    private FrameLayout layoutRoot;
    private MarqueeInfo marqueeInfo;
    private List<String> textParts;

    private int viewWidth;
    private int viewHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marquee_text, container, false);

        layoutRoot = (FrameLayout) view.findViewById(R.id.layoutRoot);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                viewWidth = right - left;
                viewHeight = bottom - top;
            }
        });

        view.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    float textSize = Math.min(viewHeight * MarqueeTextView.TEXT_SCALE, MarqueeTextView.TEXT_MAX_SIZE);
                    paint.setTextSize(textSize);
                    textParts = splitByTextWidth(marqueeInfo.getText(), viewWidth, paint);

                    for (String textPart : textParts) {
                        Log.d(TAG, "textPart: " + textPart);
                    }

                if (marqueeInfo.isBlink()) {
                    Animation blinkAnimation = new AlphaAnimation(1, 0);
                    blinkAnimation.setDuration(300);
                    blinkAnimation.setInterpolator(new LinearInterpolator());
                    blinkAnimation.setRepeatCount(Animation.INFINITE);
                    blinkAnimation.setRepeatMode(Animation.REVERSE);
                    layoutRoot.startAnimation(blinkAnimation);
                }

                } catch (Exception aE) {
                    Log.i("dlg", "run: " + aE.getMessage());
                }

                startMarquee();
            }
        });

        return view;
    }

    private List<String> splitByTextWidth(String text, int splitWidth, Paint paint) {
        List<String> strings = new ArrayList<>();

        int start = 0;
        int end = start;

        while (start < text.length()) {
            while (true) {
                end += 1;
                if (end == text.length())
                    break;

                Rect bounds = new Rect();
                paint.getTextBounds(text, start, end, bounds);
                if (bounds.width() > splitWidth)
                    break;
            }

            strings.add(text.substring(start, end));
            start = end;
        }

        return strings;
    }

    public void startMarquee() {
        startMarquee(0, null);
    }

    public void startMarquee(final int index, final Integer initFrom) {
        if (marqueeInfo == null || textParts.size() == 0)
            return;

        final MarqueeTextView marqueeTextView = new MarqueeTextView(getActivity());
        marqueeTextView.setLayoutParams(new FrameLayout.LayoutParams(viewWidth + 300, viewHeight));
        marqueeTextView.setText(textParts.get(index));
        marqueeTextView.setColor(marqueeInfo.getColor());
        marqueeTextView.setTypeface(marqueeInfo.getTypeface());
        marqueeTextView.setVisibility(View.INVISIBLE);
        layoutRoot.addView(marqueeTextView);

        marqueeTextView.post(new Runnable() {
            @Override
            public void run() {
                final float from = index == 0 ? viewWidth : initFrom;
                final float to = -1f * marqueeTextView.getTextWidthFromMeasurement();

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "start: from = " + from + ", to = " + to);
                }

                Animation animation1 = new TranslateAnimation(from, 0, 0, 0);
                animation1.setDuration((long) ((from - 0) / marqueeInfo.getSpeed()));
                animation1.setInterpolator(new LinearInterpolator());
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        /** first stage **/
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, textParts.get(index) + " arrived first stage");
                        }

                        Animation animation2 = new TranslateAnimation(0, to, 0, 0);
                        animation2.setDuration((long) ((0 - to) / marqueeInfo.getSpeed()));
                        animation2.setInterpolator(new LinearInterpolator());
                        animation2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                /** second stage **/
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, textParts.get(index) + " arrived second stage");
                                }
                                // clear self
                                marqueeTextView.clearAnimation();
                                layoutRoot.removeView(marqueeTextView);

                                int childCount = layoutRoot.getChildCount();
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "childCount: " + childCount);
                                }
                                if (childCount == 0)    // after all marquee had run over, restart marquee
                                    startMarquee();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        marqueeTextView.startAnimation(animation2); // animation from `0` to `to`

                        // prepare for following marquee
                        if (index + 1 < textParts.size()) {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, textParts.get(index + 1) + " is preparing");
                            }
                            startMarquee(index + 1, (int) marqueeTextView.getTextWidthFromMeasurement());
                        } else {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "no rest marquee");
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                marqueeTextView.startAnimation(animation1); // animation from `from` to `0`
                marqueeTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void stopMarquee() {
        for (int i = 0; i < layoutRoot.getChildCount(); ++i) {
            View v = layoutRoot.getChildAt(i);
            v.getAnimation().setAnimationListener(null);
            v.clearAnimation();
        }
        layoutRoot.removeAllViews();
    }

    public void setMarqueeInfo(MarqueeInfo marqueeInfo) {
        this.marqueeInfo = marqueeInfo;
    }

}
