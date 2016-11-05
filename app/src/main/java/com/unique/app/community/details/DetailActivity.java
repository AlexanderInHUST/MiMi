package com.unique.app.community.details;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.unique.app.community.R;
import com.unique.app.community.base.Mvp.BaseActivity;
import com.unique.app.community.base.Mvp.IView;
import com.unique.app.community.details.AskFragment.DetailAskFragment;
import com.unique.app.community.details.CommentFragment.DetailCommentFragment;
import com.unique.app.community.details.Widget.KeyboardListenerLayout;
import com.unique.app.community.details.Widget.ScrollViewWithListener;
import com.unique.app.community.entity.Event;
import com.unique.app.community.global.Conf;
import com.unique.app.community.utils.ToastUtil;
import com.unique.app.community.widget.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.unique.app.community.global.AppData.getContext;

/**
 * Author: Alexander
 * Email: yifengtang@hustunique.com
 * Since: 16/10/19.
 */

public class DetailActivity extends BaseActivity<DetailPresenter>
        implements IView {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.detail_layout)
    KeyboardListenerLayout layout;
    @BindView(R.id.detail_scroll_layout)
    ScrollViewWithListener scrollView;
    @BindView(R.id.detail_text_view_flipper)
    TextView tagOfFlipper;
    @BindView(R.id.detail_title_text_view)
    TextView mainTitle;
    @BindView(R.id.detail_text_text_view)
    TextView mainText;
    @BindView(R.id.detail_text_view_num_applied)
    TextView hasAppliedNum;
    @BindView(R.id.detail_text_view_num_joined)
    TextView hasJoinNum;
    @BindView(R.id.detail_text_view_start_time)
    TextView startTime;
    @BindView(R.id.detail_text_view_sign_up_util)
    TextView utilTime;
    @BindView(R.id.detail_text_view_place)
    TextView activityPlace;
    @BindView(R.id.detail_text_view_requirement)
    TextView requirement;
    @BindView(R.id.detail_text_view_cost)
    TextView costText;
    @BindView(R.id.detail_text_view_starter)
    TextView nameOfStarter;
    @BindView(R.id.detail_text_view_ratio)
    TextView ratioOfLike;
    @BindView(R.id.detail_layout_left_icons)
    LinearLayout leftIcons;
    @BindView(R.id.detail_layout_right_icons)
    LinearLayout rightIcons;
    @BindView(R.id.detail_image_view_starter)
    ImageView iconOfStarter;
    @BindView(R.id.detail_view_flipper)
    ViewFlipper picFlipper;
    @BindView(R.id.detail_button_wanna_join)
    Button wannaJoin;
    @BindView(R.id.detail_tab_layout_widget)
    TabLayout tabLayout;
    @BindView(R.id.detail_view_pager_fragment)
    ViewPager viewPager;
    @BindView(R.id.detail_layout_reply)
    LinearLayout replyLayout;
    @BindView(R.id.detail_edit_text_reply)
    EditText replyEditText;

    private int numLeftIcons = 0;
    private int numRightIcons = 0;
    private Handler animHolder;
    private int numFlipper = 0;
    private float slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private ArrayList<Fragment> frags;
    private boolean titleInToolbar = false;

    @Override
    protected DetailPresenter getPresenter() {
        return new DetailPresenter(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    public DetailPresenter getMPresent(){
        return mPresenter;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.attachView(this);
        Intent intent = getIntent();
        if (intent == null){
            try {
                throw new Exception("last must transmit event");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Event event = intent.getParcelableExtra(Conf.EVENT_DATA);
            mPresenter.getData(event);
        }
        initialFlipper();
        initialTab();
        initialKeyboardListener();
        initialScrollView();
        test();
    }

    private void test(){
        mPresenter.addPicToFlipper(BitmapFactory.decodeResource(getResources(), R.drawable.pic_for_test));
        mPresenter.addPicToFlipper(BitmapFactory.decodeResource(getResources(), R.drawable.pic_for_test_two));
        mPresenter.addPicToFlipper(BitmapFactory.decodeResource(getResources(), R.drawable.pic_for_test_three));
        mPresenter.setMainTitle("主图自习一天");
        mPresenter.setMainText("来来来，我们一起开启学霸模式，明天主图仔细一整天，我占好座，华科的学霸们还等什么？华科的学霸们还等什么？华科的学霸们还等什么？华科的学霸们还等什么？华科的学霸们还等什么？华科的学霸们还等什么？");
        mPresenter.setStartTime("2016.10.20 08:00");
        mPresenter.setUtilTime("2016.10.18 23:00");
        mPresenter.setActivityPlace("华中科技大学主图书馆4楼");
        mPresenter.setRequirement(2);
        mPresenter.setCost(0f);
        mPresenter.setNameOfStarter("杰克");
        mPresenter.setStarterIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.setRatioOfLike(95);
        mPresenter.addPicToAppliedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToAppliedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToAppliedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToAppliedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToAppliedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToJoinedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToJoinedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToJoinedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToJoinedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mPresenter.addPicToJoinedIcons(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }

    @OnClick(R.id.detail_button_wanna_join)
    void wannaJoin(){
        mPresenter.iWannaJoin();
    }

    /**
     *  Initial keyboard listener
     */

    private void initialKeyboardListener(){
        layout.setOnSizeChangeListener(new KeyboardListenerLayout.OnSizeChangeListener() {
            @Override
            public void onSizeChange(int w, int h, int oldW, int oldH) {
                if (oldH > h) {
                    wannaJoin.setVisibility(View.GONE);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            replyEditText.setVisibility(View.GONE);
                            replyLayout.setVisibility(View.GONE);
                            wannaJoin.setVisibility(View.VISIBLE);
                            wannaJoin.requestFocus();
                        }
                    }, 100);
                }
            }
        });
    }

    /**
     *  Initial flipper
     */

    private Animation leftInAnim;
    private Animation rightInAnim;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if(event.getY() < getResources().getDimension(R.dimen.detail_viewflipper_height) + 50) {
            super.dispatchTouchEvent(event);
            return flipperGesture.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    private Animation.AnimationListener flipperAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tagOfFlipper.setText(String.format(Locale.CHINA, "%d/%d", picFlipper.getDisplayedChild() + 1, numFlipper));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    private GestureDetector flipperGesture = new GestureDetector(mContext, new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            if (motionEvent != null && motionEvent1 != null) {
                picFlipper.setAutoStart(false);
                picFlipper.stopFlipping();
                if (Math.abs(motionEvent.getX() - motionEvent1.getX()) < slop) {
                    return false;
                } else if (motionEvent.getX() > motionEvent1.getX()) {
                    picFlipper.setInAnimation(rightInAnim);
                    picFlipper.setOutAnimation(mContext, R.anim.flipper_slide_out_left);
                    picFlipper.showNext();
                } else if (motionEvent.getX() < motionEvent1.getX()) {
                    picFlipper.setInAnimation(leftInAnim);
                    picFlipper.setOutAnimation(mContext, R.anim.flipper_slide_out_right);
                    picFlipper.showPrevious();
                }
                animHolder.removeCallbacksAndMessages(null);
                animHolder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Without these codes, the animation would get a ridiculous bug.
                        picFlipper.setInAnimation(rightInAnim);
                        picFlipper.setOutAnimation(mContext, R.anim.flipper_slide_out_left);
                        picFlipper.showNext();
                        startAutoFlipping();
                    }
                }, 3000);
                return true;
            }
            else{
                return false;
            }
        }
    });

    private void initialFlipper(){
        animHolder = new Handler();
        leftInAnim = AnimationUtils.loadAnimation(mContext, R.anim.flipper_slide_in_left);
        rightInAnim = AnimationUtils.loadAnimation(mContext, R.anim.flipper_slide_in_right);
        rightInAnim.setAnimationListener(flipperAnimListener);
        leftInAnim.setAnimationListener(flipperAnimListener);
        startAutoFlipping();
    }

    private void startAutoFlipping(){
        picFlipper.setAutoStart(true);
        picFlipper.setFlipInterval(3000);
        picFlipper.setInAnimation(rightInAnim);
        picFlipper.setOutAnimation(mContext, R.anim.flipper_slide_out_left);
        picFlipper.startFlipping();
    }

    /**
     * Initial tab layout
     */

    private void initialTab(){
        frags = new ArrayList<>();
        frags.add(mPresenter.getAskFragment());
        frags.add(mPresenter.getCommentFragment());
        viewPager.setAdapter(new DetailPageViewAdapter(getSupportFragmentManager(), frags));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                viewPager.requestLayout();
            }
        });
    }

    /**
     * Set all Texts
     */

    public void setMainTitle(String title){
        mainTitle.setText(title);
    }

    public void setMainText(String text){
        mainText.setText(text);
    }

    public void setHasAppliedNum(int num){
        hasAppliedNum.setText(String.format(Locale.CHINA, "%d人", num));
    }

    public void setHasJoinNum(int num){
        hasJoinNum.setText(String.format(Locale.CHINA, "%d人", num));
    }

    // FIXME: 16/10/22
    // Depends on the type of time data
    public void setStartTime(String time){
        startTime.setText(time);
    }

    public void setUtilTime(String time){
        utilTime.setText(time);
    }

    public void setActivityPlace(String place){
        activityPlace.setText(place);
    }

    public void setRequirement(int num){
        requirement.setText(String.format(Locale.CHINA, "%d人", num));
    }

    public void setCost(Float cost){
        if(cost.compareTo(0f) == 0)
            costText.setText(getResources().getString(R.string.for_free));
        else
            costText.setText(String.format(Locale.CHINA, "%.2f元", cost));
    }

    public void setNameOfStarter(String name){
        nameOfStarter.setText(name);
    }

    // FIXME: 16/10/22
    // Depends on type of ratio
    public void setRatioOfLike(int ratio){
        ratioOfLike.setText(String.format(Locale.CHINA, "%d%%", ratio));
    }

    // FIXME: 16/10/22
    // Depends on type of picture
    public void addPicToFlipper(Bitmap picture){
        ImageView image = new ImageView(mContext);
        image.setImageBitmap(picture);
        image.setScaleType(ImageView.ScaleType.CENTER);
        picFlipper.addView(image, numFlipper++);
        tagOfFlipper.setText(String.format(Locale.CHINA, "%d/%d", picFlipper.getDisplayedChild() + 1, numFlipper));
    }

    // FIXME: 16/10/22
    public void setStarterIcon(Bitmap icon){
        iconOfStarter.setImageBitmap(icon);
    }

    // FIXME: 16/10/22
    public void addPicToAppliedIcons(Bitmap head){
        if(numLeftIcons++ < 4) {
            float diameter = getResources().getDimension(R.dimen.detail_icon_diameter);
            LinearLayout.LayoutParams iconLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iconLayout.height = (int) diameter;
            iconLayout.width = (int) diameter;
            iconLayout.setMarginStart((int) getResources().getDimension(R.dimen.detail_margin_small_icon_horizon));
            CircularImageView icon = getIcons(head);
            icon.setBorderWidth(0);
            icon.setLayoutParams(iconLayout);
            leftIcons.addView(icon, numLeftIcons - 1);
        }else{
            // FIXME: 16/10/23
            // To change another default image
            ((ImageView)leftIcons.getChildAt(3)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.img_last_white));
        }
        setHasAppliedNum(numLeftIcons);
    }

    // FIXME: 16/10/22
    public void addPicToJoinedIcons(Bitmap head){
        if(numRightIcons++ < 4) {
            float diameter = getResources().getDimension(R.dimen.detail_icon_diameter);
            LinearLayout.LayoutParams iconLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iconLayout.height = (int) diameter;
            iconLayout.width = (int) diameter;
            iconLayout.setMarginEnd((int) getResources().getDimension(R.dimen.detail_margin_small_icon_horizon));
            CircularImageView icon = getIcons(head);
            icon.setBorderWidth(0);
            icon.setLayoutParams(iconLayout);
            rightIcons.addView(icon, numRightIcons - 1);
        }else{
            // FIXME: 16/10/23
            // To change another default image
            ((ImageView)rightIcons.getChildAt(0)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.img_last_white));
        }
        setHasJoinNum(numRightIcons);
    }

    private CircularImageView getIcons(Bitmap head){
        CircularImageView icon = new CircularImageView(mContext);
        icon.setImageBitmap(head);
        icon.setBorderWidth(0f);
        icon.setBorderColor(Color.BLACK);
        icon.setShadowRadius(0f);
        return icon;
    }

    /**
     * Reply Edit text
     */

    public void reply(int who){
        replyEditText.setText("");
        replyEditText.setVisibility(View.VISIBLE);
        replyLayout.setVisibility(View.VISIBLE);
        replyEditText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(replyEditText, InputMethod.SHOW_FORCED);
        replyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND
                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mPresenter.replyToWho(who);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Listen to scrollview
     */

    // FIXME: 16/11/5 Put text to toolbar

    private void initialScrollView(){

        scrollView.setScrollListener(new ScrollViewWithListener.OnScrollListener() {
            @Override
            public void onScroll(int y, int oldY) {
                if(y > getResources().getDimension(R.dimen.detail_viewflipper_height) + 100 + getResources().getDimension(R.dimen.detail_title_text_size)){
                    if(!titleInToolbar) {
                        ToastUtil.TextToast("Title in tool bar!");
                        titleInToolbar = true;
                    }
                }else{
                    if(titleInToolbar) {
                        ToastUtil.TextToast("Title out tool bar!");
                        titleInToolbar = false;
                    }
                }
            }
        });
    }

}
