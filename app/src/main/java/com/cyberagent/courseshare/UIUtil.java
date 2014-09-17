package com.cyberagent.courseshare;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanakasan on 9/17/2014.
 */
public class UIUtil {
	public static void animateAppearing(View target, int duration) {

		// AnimatorSetに渡すAnimatorのリスト
		List<Animator> animatorList= new ArrayList<Animator>();

		// alphaプロパティを0fから1fに変化させます
		PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat( "alpha", 0f, 1f );

		// translationYプロパティを0fからtoYに変化させます
		PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat( "translationY", -400f, 0 );

		// targetに対してholderX, holderY, holderRotationを同時に実行します
		ObjectAnimator translationXYAnimator =
				ObjectAnimator.ofPropertyValuesHolder( target, holderAlpha, holderY );
		translationXYAnimator.setDuration( duration );
		animatorList.add( translationXYAnimator );

		final AnimatorSet animatorSet = new AnimatorSet();
		// リストのAnimatorを順番に実行します
		animatorSet.playSequentially( animatorList );

		// アニメーションを開始します
		animatorSet.start();
	}
}
