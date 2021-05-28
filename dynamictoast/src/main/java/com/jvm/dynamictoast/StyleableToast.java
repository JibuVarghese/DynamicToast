package com.jvm.dynamictoast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

//        Copyright 2017 Muddi Walid
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

@SuppressLint("ViewConstructor")
public class StyleableToast extends CardView {

    private int cornerRadius;
    private int backgroundColor;
    private int strokeColor;
    private int strokeWidth;
    private int iconStart;
    //private int iconEnd;
    private int textColor;
    private int titleTextColor;
    private int lineViewColor;
    private int font;
    private int titleFont;
    private int length;
    private int style;
    private float textSize;
    private float titleTextSize;
    private boolean isTextSizeFromStyleXml = false;
    private boolean solidBackground;
    private boolean textBold;
    private boolean titleTextBold;
    private String text;
    private String titleText;
    private TypedArray typedArray;
    private TextView textViewTitle, textViewError;
    private int gravity;
    private Toast toast;
    private CardView rootLayout;
    private ImageView imageViewIcon;
    private View lineView;

    public static StyleableToast makeText(@NonNull Context context, String titleText, String errorText, int length, @StyleRes int style) {
        return new StyleableToast(context, titleText, errorText, length, style);
    }

    public static StyleableToast makeText(@NonNull Context context, String titleText, String errorText, @StyleRes int style) {
        return new StyleableToast(context, titleText, errorText, Toast.LENGTH_SHORT, style);
    }

    private StyleableToast(@NonNull Context context, String titleText, String errorText, int length, @StyleRes int style) {
        super(context);
        this.text = errorText;
        this.titleText = titleText;
        this.length = length;
        this.style = style;
    }

    private StyleableToast(Builder builder) {
        super(builder.context);
        this.backgroundColor = builder.backgroundColor;
        this.cornerRadius = builder.cornerRadius;
        //this.iconEnd = builder.iconEnd;
        this.iconStart = builder.iconStart;
        this.strokeColor = builder.strokeColor;
        this.strokeWidth = builder.strokeWidth;
        this.solidBackground = builder.solidBackground;
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.textBold = builder.textBold;
        this.font = builder.font;
        this.text = builder.text;
        this.gravity = builder.gravity;
        this.length = builder.length;

        this.titleTextColor = builder.titleTextColor;
        this.titleTextSize = builder.titleTextSize;
        this.titleTextBold = builder.titleTextBold;
        this.titleFont = builder.titleFont;
        this.titleText = builder.titleText;
        this.lineViewColor = builder.lineViewColor;
    }

    private void inflateToastLayout() {
        View v = inflate(getContext(), R.layout.styleable_layout, null);
        rootLayout = (CardView) v.getRootView();
        textViewTitle = v.findViewById(R.id.textViewTitle);
        textViewError = v.findViewById(R.id.textViewError);
        imageViewIcon = v.findViewById(R.id.imageViewIcon);
        lineView = v.findViewById(R.id.lineView);
        if (style > 0) {
            typedArray = getContext().obtainStyledAttributes(style, R.styleable.StyleableToast);
        }

        makeShape();
        makeErrorTextView();
        makeTitleTextView();
        makeIcon();

        // Very important to recycle AFTER the make() methods!
        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    private void createAndShowToast() {
        inflateToastLayout();
        toast = new Toast(getContext());
        toast.setGravity(gravity, 0, gravity == Gravity.CENTER ? 0 : toast.getYOffset());
        toast.setDuration(length == Toast.LENGTH_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(rootLayout);
        toast.show();
    }

    public void show() {
        createAndShowToast();
    }


    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }


    private void makeShape() {
        loadShapeAttributes();
        /*GradientDrawable gradientDrawable = (GradientDrawable) rootLayout.getBackground().mutate();
        gradientDrawable.setAlpha(getResources().getInteger(R.integer.defaultBackgroundAlpha));

        if (strokeWidth > 0) {
            gradientDrawable.setStroke(strokeWidth, strokeColor);
        }
        if (cornerRadius > -1) {
            gradientDrawable.setCornerRadius(cornerRadius);
        }
        if (backgroundColor != 0) {
            gradientDrawable.setColor(backgroundColor);
        }
        if (solidBackground) {
            gradientDrawable.setAlpha(getResources().getInteger(R.integer.fullBackgroundAlpha));
        }

        rootLayout.setBackground(gradientDrawable);*/
        if (lineViewColor != 0) {
            lineView.setBackgroundColor(lineViewColor);
        }

        if (cornerRadius > -1) {
            rootLayout.setRadius(cornerRadius);
        }

        if (backgroundColor != 0) {
            rootLayout.setCardBackgroundColor(backgroundColor);
        }
        if (solidBackground) {
            rootLayout.setAlpha(getResources().getInteger(R.integer.fullBackgroundAlpha));
        }
    }

    private void makeTitleTextView() {
        loadTitleTextViewAttributes();
        textViewTitle.setText(titleText);
        if (titleTextColor != 0) {
            textViewTitle.setTextColor(titleTextColor);
        }
        if (titleTextSize > 0) {
            textViewTitle.setTextSize(isTextSizeFromStyleXml ? 0 : TypedValue.COMPLEX_UNIT_SP, titleTextSize);
        }
        if (titleFont > 0) {
            textViewTitle.setTypeface(ResourcesCompat.getFont(getContext(), titleFont), titleTextBold ? Typeface.BOLD : Typeface.NORMAL);
        }
        if (titleTextBold && titleFont == 0) {
            textViewTitle.setTypeface(textViewTitle.getTypeface(), Typeface.BOLD);
        }
    }

    private void makeErrorTextView() {
        loadErrorTextViewAttributes();
        textViewError.setText(text);
        if (textColor != 0) {
            textViewError.setTextColor(textColor);
        }
        if (textSize > 0) {
            textViewError.setTextSize(isTextSizeFromStyleXml ? 0 : TypedValue.COMPLEX_UNIT_SP, textSize);
        }
        if (font > 0) {
            textViewError.setTypeface(ResourcesCompat.getFont(getContext(), font), textBold ? Typeface.BOLD : Typeface.NORMAL);
        }
        if (textBold && font == 0) {
            textViewError.setTypeface(textViewError.getTypeface(), Typeface.BOLD);
        }
    }


    private void makeIcon() {
        loadIconAttributes();
        int paddingVertical = (int) getResources().getDimension(R.dimen.toast_vertical_padding);
        int paddingHorizontal1 = (int) getResources().getDimension(R.dimen.toast_horizontal_padding_icon_side);
        int paddingNoIcon = (int) getResources().getDimension(R.dimen.toast_horizontal_padding_empty_side);
        int iconSize = (int) getResources().getDimension(R.dimen.icon_size);
        if (iconStart != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconStart);
            if (drawable != null) {
                /*ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(iconSize, iconSize);
                imageViewIcon.setLayoutParams(layoutParams);*/
                imageViewIcon.setImageDrawable(drawable);
            }
        }
        /*if (iconStart != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconStart);
            if (drawable != null) {
                drawable.setBounds(0, 0, iconSize, iconSize);
                TextViewCompat.setCompoundDrawablesRelative(textView, drawable, null, null, null);
                if (Utils.isRTL()) {
                    rootLayout.setPadding(paddingNoIcon, paddingVertical, paddingHorizontal1, paddingVertical);
                } else {
                    rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingNoIcon, paddingVertical);
                }
            }
        }

        if (iconEnd != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconEnd);
            if (drawable != null) {
                drawable.setBounds(0, 0, iconSize, iconSize);
                TextViewCompat.setCompoundDrawablesRelative(textView, null, null, drawable, null);
                if (Utils.isRTL()) {
                    rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingNoIcon, paddingVertical);
                } else {
                    rootLayout.setPadding(paddingNoIcon, paddingVertical, paddingHorizontal1, paddingVertical);
                }
            }
        }

        if (iconStart != 0 && iconEnd != 0) {
            Drawable drawableLeft = ContextCompat.getDrawable(getContext(), iconStart);
            Drawable drawableRight = ContextCompat.getDrawable(getContext(), iconEnd);
            if (drawableLeft != null && drawableRight != null) {
                drawableLeft.setBounds(0, 0, iconSize, iconSize);
                drawableRight.setBounds(0, 0, iconSize, iconSize);
                textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
                rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingHorizontal1, paddingVertical);
            }
        }*/
    }

    /**
     * loads style attributes from styles.xml if a style resource is used.
     */

    private void loadShapeAttributes() {
        if (style == 0) {
            return;
        }

        int defaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.default_background_color);
        int defaultLineViewColor = ContextCompat.getColor(getContext(), R.color.default_stroke_view_color);
        int defaultCornerRadius = (int) getResources().getDimension(R.dimen.default_corner_radius);

        solidBackground = typedArray.getBoolean(R.styleable.StyleableToast_stSolidBackground, false);
        backgroundColor = typedArray.getColor(R.styleable.StyleableToast_stColorBackground, defaultBackgroundColor);
        cornerRadius = (int) typedArray.getDimension(R.styleable.StyleableToast_stRadius, defaultCornerRadius);
        length = typedArray.getInt(R.styleable.StyleableToast_stLength, 0);
        gravity = typedArray.getInt(R.styleable.StyleableToast_stGravity, Gravity.BOTTOM);
        lineViewColor = typedArray.getColor(R.styleable.StyleableToast_stLineViewColor, defaultLineViewColor);


        if (gravity == 1) {
            gravity = Gravity.CENTER;
        } else if (gravity == 2) {
            gravity = Gravity.TOP;
        }

        if (typedArray.hasValue(R.styleable.StyleableToast_stStrokeColor) && typedArray.hasValue(R.styleable.StyleableToast_stStrokeWidth)) {
            strokeWidth = (int) typedArray.getDimension(R.styleable.StyleableToast_stStrokeWidth, 0);
            strokeColor = typedArray.getColor(R.styleable.StyleableToast_stStrokeColor, Color.TRANSPARENT);
        }
    }

    private void loadTitleTextViewAttributes() {
        if (style == 0) {
            return;
        }

        titleTextColor = typedArray.getColor(R.styleable.StyleableToast_stTitleTextColor, textViewTitle.getCurrentTextColor());
        titleTextBold = typedArray.getBoolean(R.styleable.StyleableToast_stTitleTextBold, false);
        titleTextSize = typedArray.getDimension(R.styleable.StyleableToast_stTitleTextSize, 0);
        titleFont = typedArray.getResourceId(R.styleable.StyleableToast_stTitleFont, 0);
        isTextSizeFromStyleXml = textSize > 0;
    }

    private void loadErrorTextViewAttributes() {
        if (style == 0) {
            return;
        }

        textColor = typedArray.getColor(R.styleable.StyleableToast_stTextColor, textViewTitle.getCurrentTextColor());
        textBold = typedArray.getBoolean(R.styleable.StyleableToast_stTextBold, false);
        textSize = typedArray.getDimension(R.styleable.StyleableToast_stTextSize, 0);
        font = typedArray.getResourceId(R.styleable.StyleableToast_stFont, 0);
        isTextSizeFromStyleXml = textSize > 0;
    }


    private void loadIconAttributes() {
        if (style == 0) {
            return;
        }
        iconStart = typedArray.getResourceId(R.styleable.StyleableToast_stIconStart, 0);
        //iconEnd = typedArray.getResourceId(R.styleable.StyleableToast_stIconEnd, 0);
    }

    public static class Builder {
        private int cornerRadius = -1;
        private int backgroundColor;
        private int strokeColor;
        private int strokeWidth;
        private int iconStart;
        //private int iconEnd;
        private int textColor;
        private int font;
        private int length;
        private float textSize;
        private boolean solidBackground;
        private boolean textBold;
        private String text;
        private int gravity = Gravity.BOTTOM;
        private StyleableToast toast;
        private final Context context;
        private int titleTextColor;
        private int lineViewColor;
        private int titleFont;
        private float titleTextSize;
        private boolean titleTextBold;
        private String titleText;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder titleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public Builder textColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder titleTextColor(@ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder lineViewColor(@ColorInt int lineViewColor) {
            this.lineViewColor = lineViewColor;
            return this;
        }

        public Builder textBold() {
            this.textBold = true;
            return this;
        }

        public Builder titleTextBold() {
            this.titleTextBold = true;
            return this;
        }

        public Builder textSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder titleTextSize(float titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        /**
         * @param font A font resource id like R.font.somefont as introduced with the new font api in Android 8
         */
        public Builder font(@FontRes int font) {
            this.font = font;
            return this;
        }

        public Builder titleFont(@FontRes int titleFont) {
            this.titleFont = titleFont;
            return this;
        }

        public Builder backgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        /**
         * This call will make the StyleableToast's background completely solid without any opacity.
         */
        public Builder solidBackground() {
            this.solidBackground = true;
            return this;
        }

        public Builder stroke(int strokeWidth, @ColorInt int strokeColor) {
            this.strokeWidth = Utils.toDp(context, strokeWidth);
            this.strokeColor = strokeColor;
            return this;
        }

        /**
         * @param cornerRadius Sets the corner radius of the StyleableToast's shape.
         */
        public Builder cornerRadius(int cornerRadius) {
            this.cornerRadius = Utils.toDp(context, cornerRadius);
            return this;
        }

        public Builder iconStart(@DrawableRes int iconStart) {
            this.iconStart = iconStart;
            return this;
        }

        /*public Builder iconEnd(@DrawableRes int iconEnd) {
            this.iconEnd = iconEnd;
            return this;
        }*/

        /**
         * Sets where the StyleableToast will appear on the screen
         */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * @param length {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
         */
        public Builder length(int length) {
            this.length = length;
            return this;
        }

        /**
         * @return an mutable instance of the build
         */
        public Builder build() {
            return this;
        }

        public void show() {
            toast = new StyleableToast(this);
            toast.show();
        }

        public void cancel() {
            if (toast != null) {
                toast.cancel();
            }
        }
    }
}
