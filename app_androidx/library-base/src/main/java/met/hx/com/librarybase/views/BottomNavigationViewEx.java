package met.hx.com.librarybase.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Transition;
import androidx.transition.TransitionSet;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by yu on 2016/11/10.
 * https://github.com/ittianyu/BottomNavigationViewEx
 */
@SuppressLint("RestrictedApi")
public class BottomNavigationViewEx extends BottomNavigationView {
    // used for animation
    private float shiftAmount;
    private float scaleUpFactor;
    private float scaleDownFactor;
    private boolean animationRecord;
    private float mLargeLabelSize;
    private float mSmallLabelSize;
    private boolean visibilityTextSizeRecord;
    private boolean visibilityHeightRecord;
    private int itemHeight;
    private boolean textVisibility = true;
    // used for animation end

    // used for setupWithViewPager
    private ViewPager mViewPager;
    private MyOnNavigationItemSelectedListener mMyOnNavigationItemSelectedListener;
    private BottomNavigationViewExOnPageChangeListener mPageChangeListener;
    private BottomNavigationMenuView mMenuView;
    private BottomNavigationItemView[] buttons;
    // used for setupWithViewPager end

    public BottomNavigationViewEx(Context context) {
        super(context);
//        init();
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    private void init() {
        try {
            addAnimationListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addAnimationListener() {
        /**
         * 1. BottomNavigationMenuView mMenuView
         * 2. private final BottomNavigationAnimationHelperBase mAnimationHelper;
         * 3. private final TransitionSet mSet;
         */
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        Object mAnimationHelper = getField(mMenuView.getClass(), mMenuView, "mAnimationHelper");
        TransitionSet mSet = getField(mAnimationHelper.getClass(), mAnimationHelper, "mSet");
        mSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                refreshTextViewVisibility();
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {
                refreshTextViewVisibility();
            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {
            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {
            }
        });
    }

    private void refreshTextViewVisibility() {
        if (!textVisibility)
            return;
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();

        int currentItem = getCurrentItem();

        // 3. get field shiftingMode and TextView in buttons
        for (BottomNavigationItemView button : buttons) {
            TextView largeLabel = getField(button.getClass(), button, "largeLabel");
            TextView smallLabel = getField(button.getClass(), button, "smallLabel");

            largeLabel.clearAnimation();
            smallLabel.clearAnimation();

            // shiftingMode
            boolean shiftingMode = getField(button.getClass(), button, "shiftingMode");
            boolean selected = button.getItemPosition() == currentItem;
            if (shiftingMode) {
                if (selected) {
                    largeLabel.setVisibility(VISIBLE);
                } else {
                    largeLabel.setVisibility(INVISIBLE);
                }
                smallLabel.setVisibility(INVISIBLE);
            } else {
                if (selected) {
                    largeLabel.setVisibility(VISIBLE);
                    smallLabel.setVisibility(INVISIBLE);
                } else {
                    largeLabel.setVisibility(INVISIBLE);
                    smallLabel.setVisibility(VISIBLE);
                }
            }
        }
    }


    /**
     * change the visibility of icon
     *
     * @param visibility
     */
    public void setIconVisibility(boolean visibility) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in buttons
        private BottomNavigationItemView[] buttons;

        3. get icon in buttons
        private ImageView icon

        4. set icon visibility gone

        5. change itemHeight to only text size in mMenuView
         */
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();
        // 3. get icon in buttons
        for (BottomNavigationItemView button : buttons) {
            ImageView icon = getField(button.getClass(), button, "icon");
            // 4. set icon visibility gone
            icon.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        }

        // 5. change itemHeight to only text size in mMenuView
        if (!visibility) {
            // if not record itemHeight
            if (!visibilityHeightRecord) {
                visibilityHeightRecord = true;
                itemHeight = getItemHeight();
            }

            // change itemHeight
            BottomNavigationItemView button = buttons[0];
            if (null != button) {
                final ImageView icon = getField(button.getClass(), button, "icon");
//                System.out.println("icon.getMeasuredHeight():" + icon.getMeasuredHeight());
                if (null != icon) {
                    icon.post(new Runnable() {
                        @Override
                        public void run() {
//                            System.out.println("icon.getMeasuredHeight():" + icon.getMeasuredHeight());
                            setItemHeight(itemHeight - icon.getMeasuredHeight());
                        }
                    });
                }
            }
        } else {
            // if not record the itemHeight, we need do nothing.
            if (!visibilityHeightRecord)
                return;

            // restore it
            setItemHeight(itemHeight);
        }

        mMenuView.updateMenuView();
    }

    /**
     * change the visibility of text
     *
     * @param visibility
     */
    public void setTextVisibility(boolean visibility) {
        this.textVisibility = visibility;
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in buttons
        private BottomNavigationItemView[] buttons;

        3. set text size in buttons
        private final TextView largeLabel
        private final TextView smallLabel

        4. change itemHeight to only icon size in mMenuView
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();

        // 3. change field shiftingMode value in buttons
        for (BottomNavigationItemView button : buttons) {
            TextView largeLabel = getField(button.getClass(), button, "largeLabel");
            TextView smallLabel = getField(button.getClass(), button, "smallLabel");

            if (!visibility) {
                // if not record the font size, record it
                if (!visibilityTextSizeRecord && !animationRecord) {
                    visibilityTextSizeRecord = true;
                    mLargeLabelSize = largeLabel.getTextSize();
                    mSmallLabelSize = smallLabel.getTextSize();
                }

                // if not visitable, set font size to 0
                largeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);
                smallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);

            } else {
                // if not record the font size, we need do nothing.
                if (!visibilityTextSizeRecord)
                    break;

                // restore it
                largeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
                smallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);
            }
        }

        // 4 change itemHeight to only icon size in mMenuView
        if (!visibility) {
            // if not record itemHeight
            if (!visibilityHeightRecord) {
                visibilityHeightRecord = true;
                itemHeight = getItemHeight();
            }

            // change itemHeight to only icon size in mMenuView
            // private final int itemHeight;

            // change itemHeight
//            System.out.println("largeLabel.getMeasuredHeight():" + getFontHeight(mSmallLabelSize));
            setItemHeight(itemHeight - getFontHeight(mSmallLabelSize));

        } else {
            // if not record the itemHeight, we need do nothing.
            if (!visibilityHeightRecord)
                return;
            // restore itemHeight
            setItemHeight(itemHeight);
        }

        mMenuView.updateMenuView();
    }

    /**
     * get text height by font size
     *
     * @param fontSize
     * @return
     */
    private static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * enable or disable click item animation(text scale and icon move animation in no item shifting mode)
     *
     * @param enable It means the text won't scale and icon won't move when active it in no item shifting mode if false.
     */
    public void enableAnimation(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in buttons
        private BottomNavigationItemView[] buttons;

        3. chang shiftAmount to 0 in buttons
        private final int shiftAmount

        change scaleUpFactor and scaleDownFactor to 1f in buttons
        private final float scaleUpFactor
        private final float scaleDownFactor

        4. change label font size in buttons
        private final TextView largeLabel
        private final TextView smallLabel
         */

        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();
        // 3. change field shiftingMode value in buttons
        for (BottomNavigationItemView button : buttons) {
            TextView largeLabel = getField(button.getClass(), button, "smallLabel");
            TextView smallLabel = getField(button.getClass(), button, "largeLabel");

            // if disable animation, need animationRecord the source value
            if (!enable) {
                if (!animationRecord) {
                    animationRecord = true;
                    shiftAmount = getField(button.getClass(), button, "shiftAmount");
                    scaleUpFactor = getField(button.getClass(), button, "scaleUpFactor");
                    scaleDownFactor = getField(button.getClass(), button, "scaleDownFactor");

                    mLargeLabelSize = largeLabel.getTextSize();
                    mSmallLabelSize = smallLabel.getTextSize();

//                    System.out.println("shiftAmount:" + shiftAmount + " scaleUpFactor:"
//                            + scaleUpFactor + " scaleDownFactor:" + scaleDownFactor
//                            + " largeLabel:" + mLargeLabelSize + " smallLabel:" + mSmallLabelSize);
                }
                // disable
                setField(button.getClass(), button, "shiftAmount", 0);
                setField(button.getClass(), button, "scaleUpFactor", 1);
                setField(button.getClass(), button, "scaleDownFactor", 1);

                // let the largeLabel font size equal to smallLabel
                largeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);

                // debug start
//                mLargeLabelSize = largeLabel.getTextSize();
//                System.out.println("largeLabel:" + mLargeLabelSize);
                // debug end

            } else {
                // haven't change the value. It means it was the first call this method. So nothing need to do.
                if (!animationRecord)
                    return;
                // enable animation
                setField(button.getClass(), button, "shiftAmount", shiftAmount);
                setField(button.getClass(), button, "scaleUpFactor", scaleUpFactor);
                setField(button.getClass(), button, "scaleDownFactor", scaleDownFactor);
                // restore
                largeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
            }
        }
        mMenuView.updateMenuView();
    }

    /**
     * enable the shifting mode for navigation
     *
     * @param enable It will has a shift animation if true. Otherwise all items are the same width.
     */
    public void enableShiftingMode(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. change field shiftingMode value in mMenuView
        private boolean shiftingMode = true;
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. change field shiftingMode value in mMenuView
        setField(mMenuView.getClass(), mMenuView, "shiftingMode", enable);

        mMenuView.updateMenuView();
    }

    /**
     * enable the shifting mode for each item
     *
     * @param enable It will has a shift animation for item if true. Otherwise the item text always be shown.
     */
    public void enableItemShiftingMode(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in this mMenuView
        private BottomNavigationItemView[] buttons;

        3. change field shiftingMode value in buttons
        private boolean shiftingMode = true;
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();
        // 3. change field shiftingMode value in buttons
        for (BottomNavigationItemView button : buttons) {
            setField(button.getClass(), button, "shiftingMode", enable);
        }
        mMenuView.updateMenuView();
    }

    /**
     * get the current checked item position
     *
     * @return index of item, start from 0.
     */
    public int getCurrentItem() {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mMenuView
        private BottomNavigationItemView[] buttons;

        3. get menu and traverse it to get the checked one
         */

        // 1. get mMenuView
//        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();
        // 3. get menu and traverse it to get the checked one
        Menu menu = getMenu();
        for (int i = 0; i < buttons.length; i++) {
            if (menu.getItem(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * get menu item position in menu
     *
     * @param item
     * @return position if success, -1 otherwise
     */
    public int getMenuItemPosition(MenuItem item) {
        // get item id
        int itemId = item.getItemId();
        // get meunu
        Menu menu = getMenu();
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            if (menu.getItem(i).getItemId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * set the current checked item
     *
     * @param item start from 0.
     */
    public void setCurrentItem(int item) {
        // check bounds
        if (item < 0 || item >= getMaxItemCount()) {
            throw new ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - "
                    + (getMaxItemCount() - 1) + ". Actually " + item);
        }

        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;

        2. get field in mMenuView
        private BottomNavigationItemView[] buttons;
        private final OnClickListener onClickListener;

        3. call onClickListener.onClick();
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get buttons
        BottomNavigationItemView[] buttons = getBottomNavigationItemViews();
        // get onClickListener
        View.OnClickListener onClickListener = getField(mMenuView.getClass(), mMenuView, "onClickListener");

//        System.out.println("mMenuView:" + mMenuView + " buttons:" + buttons + " onClickListener" + onClickListener);
        // 3. call onClickListener.onClick();
        onClickListener.onClick(buttons[item]);

    }

    /**
     * get OnNavigationItemSelectedListener
     *
     * @return
     */
    public OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        // private OnNavigationItemSelectedListener mListener;
        OnNavigationItemSelectedListener mListener = getField(BottomNavigationView.class, this, "selectedListener");
        return mListener;
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        // if not set up with view pager, the same with father
        if (null == mMyOnNavigationItemSelectedListener) {
            super.setOnNavigationItemSelectedListener(listener);
            return;
        }

        mMyOnNavigationItemSelectedListener.setOnNavigationItemSelectedListener(listener);
    }

    /**
     * get private mMenuView
     *
     * @return
     */
    private BottomNavigationMenuView getBottomNavigationMenuView() {
        if (null == mMenuView)
            mMenuView = getField(BottomNavigationView.class, this, "menuView");
        return mMenuView;
    }

    /**
     * get private buttons in mMenuView
     *
     * @return
     */
    public BottomNavigationItemView[] getBottomNavigationItemViews() {
        if (null != buttons)
            return buttons;
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] buttons;
         */
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        buttons = getField(mMenuView.getClass(), mMenuView, "buttons");
        return buttons;
    }

    /**
     * get private mButton in mMenuView at position
     *
     * @param position
     * @return
     */
    public BottomNavigationItemView getBottomNavigationItemView(int position) {
        return getBottomNavigationItemViews()[position];
    }

    /**
     * get icon at position
     *
     * @param position
     * @return
     */
    public ImageView getIconAt(int position) {
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] buttons;
         * 3 private ImageView icon;
         */
        BottomNavigationItemView buttons = getBottomNavigationItemView(position);
        ImageView icon = getField(BottomNavigationItemView.class, buttons, "icon");
        return icon;
    }

    /**
     * get small label at position
     * Each item has tow label, one is large, another is small.
     *
     * @param position
     * @return
     */
    public TextView getSmallLabelAt(int position) {
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] buttons;
         * 3 private final TextView smallLabel;
         */
        BottomNavigationItemView buttons = getBottomNavigationItemView(position);
        TextView smallLabel = getField(BottomNavigationItemView.class, buttons, "smallLabel");
        return smallLabel;
    }

    /**
     * get large label at position
     * Each item has tow label, one is large, another is small.
     *
     * @param position
     * @return
     */
    public TextView getLargeLabelAt(int position) {
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] buttons;
         * 3 private final TextView largeLabel;
         */
        BottomNavigationItemView buttons = getBottomNavigationItemView(position);
        TextView largeLabel = getField(BottomNavigationItemView.class, buttons, "largeLabel");
        return largeLabel;
    }

    /**
     * return item count
     *
     * @return
     */
    public int getItemCount() {
        BottomNavigationItemView[] bottomNavigationItemViews = getBottomNavigationItemViews();
        if (null == bottomNavigationItemViews)
            return 0;
        return bottomNavigationItemViews.length;
    }

    /**
     * set all item small TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setSmallTextSize(float sp) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getSmallLabelAt(i).setTextSize(sp);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set all item large TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal.
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setLargeTextSize(float sp) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTextSize(sp);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set all item large and small TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setTextSize(float sp) {
        setLargeTextSize(sp);
        setSmallTextSize(sp);
    }

    /**
     * set item ImageView size which at position
     *
     * @param position position start from 0
     * @param width    in dp
     * @param height   in dp
     */
    public void setIconSizeAt(int position, float width, float height) {
        ImageView icon = getIconAt(position);
        // update size
        ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
        layoutParams.width = dp2px(getContext(), width);
        layoutParams.height = dp2px(getContext(), height);
        icon.setLayoutParams(layoutParams);

        mMenuView.updateMenuView();
    }

    /**
     * set all item ImageView size
     *
     * @param width  in dp
     * @param height in dp
     */
    public void setIconSize(float width, float height) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            setIconSizeAt(i, width, height);
        }
    }

    /**
     * set menu item height
     *
     * @param height in px
     */
    public void setItemHeight(int height) {
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. set private final int itemHeight in mMenuView
        setField(mMenuView.getClass(), mMenuView, "itemHeight", height);

        mMenuView.updateMenuView();
    }

    /**
     * get menu item height
     *
     * @return in px
     */
    public int getItemHeight() {
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get private final int itemHeight in mMenuView
        return getField(mMenuView.getClass(), mMenuView, "itemHeight");
    }

    /**
     * dp to px
     *
     * @param context
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * set Typeface for all item TextView
     *
     * @attr ref android.R.styleable#TextView_typeface
     * @attr ref android.R.styleable#TextView_textStyle
     */
    public void setTypeface(Typeface typeface, int style) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTypeface(typeface, style);
            getSmallLabelAt(i).setTypeface(typeface, style);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set Typeface for all item TextView
     *
     * @attr ref android.R.styleable#TextView_typeface
     */
    public void setTypeface(Typeface typeface) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTypeface(typeface);
            getSmallLabelAt(i).setTypeface(typeface);
        }
        mMenuView.updateMenuView();
    }

    /**
     * get private filed in this specific object
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param <T>
     * @return field if success, null otherwise.
     */
    private <T> T getField(Class targetClass, Object instance, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * change the field value
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param value
     */
    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will link the given ViewPager and this BottomNavigationViewEx together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks.
     *
     * @param viewPager
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager) {
        setupWithViewPager(viewPager, false);
    }

    /**
     * This method will link the given ViewPager and this BottomNavigationViewEx together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks.
     *
     * @param viewPager
     * @param smoothScroll whether ViewPager changed with smooth scroll animation
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager, boolean smoothScroll) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
        }

        if (null == viewPager) {
            mViewPager = null;
            super.setOnNavigationItemSelectedListener(null);
            return;
        }

        mViewPager = viewPager;

        // Add our custom OnPageChangeListener to the ViewPager
        if (mPageChangeListener == null) {
            mPageChangeListener = new BottomNavigationViewExOnPageChangeListener(this);
        }
        viewPager.addOnPageChangeListener(mPageChangeListener);

        // Now we'll add a navigation item selected listener to set ViewPager's current item
        OnNavigationItemSelectedListener listener = getOnNavigationItemSelectedListener();
        mMyOnNavigationItemSelectedListener = new MyOnNavigationItemSelectedListener(viewPager, this, smoothScroll, listener);
        super.setOnNavigationItemSelectedListener(mMyOnNavigationItemSelectedListener);
    }

    /**
     * A {@link ViewPager.OnPageChangeListener} class which contains the
     * necessary calls back to the provided {@link BottomNavigationViewEx} so that the tab position is
     * kept in sync.
     * <p>
     * <p>This class stores the provided BottomNavigationViewEx weakly, meaning that you can use
     * {@link ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)
     * addOnPageChangeListener(OnPageChangeListener)} without removing the listener and
     * not cause a leak.
     */
    private static class BottomNavigationViewExOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<BottomNavigationViewEx> mBnveRef;

        public BottomNavigationViewExOnPageChangeListener(BottomNavigationViewEx bnve) {
            mBnveRef = new WeakReference<>(bnve);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            final BottomNavigationViewEx bnve = mBnveRef.get();
            if (null != bnve)
                bnve.setCurrentItem(position);
//            Log.d("onPageSelected", "--------- position " + position + " ------------");
        }
    }

    /**
     * Decorate OnNavigationItemSelectedListener for setupWithViewPager
     */
    private static class MyOnNavigationItemSelectedListener implements OnNavigationItemSelectedListener {
        private OnNavigationItemSelectedListener listener;
        private final WeakReference<ViewPager> viewPagerRef;
        private boolean smoothScroll;
        private SparseIntArray items;// used for change ViewPager selected item
        private int previousPosition = -1;


        MyOnNavigationItemSelectedListener(ViewPager viewPager, BottomNavigationViewEx bnve, boolean smoothScroll, OnNavigationItemSelectedListener listener) {
            this.viewPagerRef = new WeakReference<>(viewPager);
            this.listener = listener;
            this.smoothScroll = smoothScroll;

            // create items
            Menu menu = bnve.getMenu();
            int size = menu.size();
            items = new SparseIntArray(size);
            for (int i = 0; i < size; i++) {
                int itemId = menu.getItem(i).getItemId();
                items.put(itemId, i);
            }
        }

        public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int position = items.get(item.getItemId());
            // only set item when item changed
            if (previousPosition == position) {
                return true;
            }

            // user listener
            if (null != listener) {
                boolean bool = listener.onNavigationItemSelected(item);
                // if the selected is invalid, no need change the view pager
                if (!bool)
                    return false;
            }

            // change view pager
            ViewPager viewPager = viewPagerRef.get();
            if (null == viewPager)
                return false;

            viewPager.setCurrentItem(items.get(item.getItemId()), smoothScroll);

            // update previous position
            previousPosition = position;

            return true;
        }

    }

    public void enableShiftingMode(int position, boolean enable) {
        getBottomNavigationItemView(position).setShifting(enable);
    }

    public void setItemBackground(int position, int background) {
        getBottomNavigationItemView(position).setItemBackground(background);
    }

    public void setIconTintList(int position, ColorStateList tint) {
        getBottomNavigationItemView(position).setIconTintList(tint);
    }

    public void setTextTintList(int position, ColorStateList tint) {
        getBottomNavigationItemView(position).setTextColor(tint);
    }

    /**
     * set margin top for all icons
     *
     * @param marginTop in px
     */
    public void setIconsMarginTop(int marginTop) {
        for (int i = 0; i < getItemCount(); i++) {
            setIconMarginTop(i, marginTop);
        }
    }

    /**
     * set margin top for icon
     *
     * @param position
     * @param marginTop in px
     */
    public void setIconMarginTop(int position, int marginTop) {
        /*
        1. BottomNavigationItemView
        2. private final int defaultMargin;
         */
        BottomNavigationItemView itemView = getBottomNavigationItemView(position);
        setField(BottomNavigationItemView.class, itemView, "defaultMargin", marginTop);
        mMenuView.updateMenuView();
    }

}
