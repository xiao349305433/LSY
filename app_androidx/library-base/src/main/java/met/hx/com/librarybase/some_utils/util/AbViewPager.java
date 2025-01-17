/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package met.hx.com.librarybase.some_utils.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbViewPager.java 
 * 描述：可设置是否滑动的ViewPager.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbViewPager extends ViewPager {

	/** The enabled. */
	private boolean enabled;
	private boolean enabledScrollAnim=true;

	/**
	 * Instantiates a new ab un slide view pager.
	 *
	 * @param context the context
	 */
	public AbViewPager(Context context) {
		super(context);
		this.enabled = true;
	}
	
	/**
	 * Instantiates a new ab un slide view pager.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	/**
	 * 描述：触摸没有反应就可以了.
	 *
	 * @param event the event
	 * @return true, if successful
	 * @see ViewPager#onTouchEvent(MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param event the event
	 * @return true, if successful
	 * @see ViewPager#onInterceptTouchEvent(MotionEvent)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:50
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item, enabledScrollAnim);
	}
	/**
	 * Sets the paging enabled.
	 *
	 * @param enabled the new paging enabled
	 */
	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setEnabledScrollAnim(boolean enabledScrollAnim) {
		this.enabledScrollAnim = enabledScrollAnim;
	}
}
