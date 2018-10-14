package com.yason.doubanmovie.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import com.yason.doubanmovie.R;
import com.yason.doubanmovie.common.util.dimen.DimenUtil;

/**
 * @author Yason
 * @since 2018/10/11
 */

public class ExpandableTextView extends android.support.v7.widget.AppCompatTextView {

  private String mOriginText;
  private StaticLayout mTextLayout;

  private int mMaxExpandLines;
  private String mExpandBtnText;
  private String mCloseBtnText;
  private int mExpandBtnTextColor;
  private int mCloseBtnTextColor;

  private SpannableString mExpandBtnSpan;
  private SpannableString mCloseBtnSpan;

  int mViewWidth = DimenUtil.getScreenWidth();

  private static final int DEAFULT_MAX_EXPAND_LINES = 4;
  private static final String DEAFULT_EXPAND_BTN_TEXT = "全部展开";
  private static final String DEAFULT_CLOSE_BTN_TEXT = "收起";
  private static final int DEAFULT_EXPAND_BTN_TEXT_COLOR = R.color.dark_green;
  private static final int DEAFULT_CLOSE_BTN_TEXT_COLOR = R.color.dark_blue;


  public ExpandableTextView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
    mMaxExpandLines = ta.getInteger(R.styleable.ExpandableTextView_maxExpandLines, DEAFULT_MAX_EXPAND_LINES);
    mExpandBtnText = ta.getString(R.styleable.ExpandableTextView_expandBtnText);
    if (mExpandBtnText == null) {
      mExpandBtnText = DEAFULT_EXPAND_BTN_TEXT;
    }
    mCloseBtnText = ta.getString(R.styleable.ExpandableTextView_closeBtnText);
    if (mCloseBtnText == null) {
      mCloseBtnText = DEAFULT_CLOSE_BTN_TEXT;
    }
    mExpandBtnTextColor = ta.getColor(R.styleable.ExpandableTextView_expandBtnTextColor, getResources().getColor(DEAFULT_EXPAND_BTN_TEXT_COLOR));
    mCloseBtnTextColor = ta.getInteger(R.styleable.ExpandableTextView_closeBtnTextColor, getResources().getColor(DEAFULT_CLOSE_BTN_TEXT_COLOR));
    ta.recycle();

    mOriginText = getText().toString();
    mTextLayout = createTextLayout(mOriginText);

    if (mTextLayout.getLineCount() > mMaxExpandLines) {
      closeTextView();
    }
  }

  public void setContent(CharSequence text) {
    mOriginText = text.toString();
    mTextLayout = createTextLayout(mOriginText);
    if (mTextLayout.getLineCount() > mMaxExpandLines) {
      closeTextView();
    } else {
      setText(mOriginText);
    }
  }

  private void expandTextView() {
    setMaxLines(Integer.MAX_VALUE);

    String content = mOriginText + "  ";
    setText(content);

    if (mCloseBtnSpan == null) {
      initCloseSpan();
    }
    append(mCloseBtnSpan);
    setMovementMethod(LinkMovementMethod.getInstance());
  }

  private void closeTextView() {
    setMaxLines(mMaxExpandLines);

    //获取到第maxline行最后一个文字的下标
    int index = mTextLayout.getLineStart(mMaxExpandLines) - 1;
    //定义收起后的文本内容
    String suffix = "..." + mExpandBtnText;
    String content = mOriginText.substring(0, index - suffix.length()) + "...";
    setText(content);

    if (mExpandBtnSpan == null) {
      initExpandBtn();
    }
    append(mExpandBtnSpan);
    setMovementMethod(LinkMovementMethod.getInstance());
  }

  private void initExpandBtn() {
    ButtonSpan expandBtnSpan = new ButtonSpan(new OnClickListener() {
      @Override
      public void onClick(View v) {
        expandTextView();
      }
    }, mExpandBtnTextColor);
    mExpandBtnSpan = new SpannableString(mExpandBtnText);
    mExpandBtnSpan.setSpan(expandBtnSpan, 0, mExpandBtnText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
  }

  private void initCloseSpan() {
    ButtonSpan closeBtnSpan = new ButtonSpan(new OnClickListener() {
      @Override
      public void onClick(View v) {
        closeTextView();
      }
    }, mCloseBtnTextColor);
    mCloseBtnSpan = new SpannableString(mCloseBtnText);
    mCloseBtnSpan.setSpan(closeBtnSpan, 0, mCloseBtnText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
  }

  private StaticLayout createTextLayout(String text) {
    return new StaticLayout(text, getPaint(),
        mViewWidth - getPaddingLeft() - getPaddingRight(), Layout.Alignment.ALIGN_NORMAL,
        getLineSpacingMultiplier(), getLineSpacingExtra(), false);
  }

  private class ButtonSpan extends ClickableSpan {

    private OnClickListener mOnClickListener;
    private int mTextColor;

    ButtonSpan(OnClickListener listener, int color) {
      mOnClickListener = listener;
      mTextColor = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
      ds.setColor(mTextColor);
      ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
      if (mOnClickListener != null) {
        mOnClickListener.onClick(widget);
      }
    }
  }

}
