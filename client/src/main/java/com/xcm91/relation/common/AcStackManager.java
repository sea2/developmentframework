package com.xcm91.relation.common;

import android.app.Activity;

import com.xcm91.relation.util.LogManager;

import java.util.Stack;

/**
 * 自定义activity栈的管理
 * 
 * @author yejingjie
 * 
 */
public class AcStackManager {
	private static Stack<Activity> activityStack;
	private static AcStackManager instance;

	private AcStackManager() {
	}

	public static AcStackManager getInstance() {
		if (instance == null) {
			synchronized (AcStackManager.class) {
				if (instance == null) {
					instance = new AcStackManager();
				}
			}
		}
		return instance;
	}

	/**
	 * 销毁栈顶的activity并从栈中移除
	 * 
	 * @param isDestroy
	 *            是否需要手动销毁
	 */
	public void popActivity(boolean isDestroy) {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			if (isDestroy) {
				activity.finish();
			}
			activity = null;
		}
	}

	/**
	 * 销毁栈中某个activity并从栈中移除
	 * 
	 * @param isDestroy
	 *            是否需要手动销毁
	 */
	public void popActivity(Activity activity, boolean isDestroy) {
		if (activity != null) {
			activityStack.remove(activity);
			if (isDestroy) {
				activity.finish();
			}
			activity = null;
		}
	}

	/**
	 * 销毁栈中某个activity并从栈中移除
	 * 
	 * @param isDestroy
	 *            是否需要手动销毁
	 */
	public void popActivity(Class<?> activityClass) {
		if (activityStack != null && activityStack.size() > 0) {
			for (Activity ac : activityStack) {
				if (ac != null && ac.getClass() == activityClass) {
					popActivity(true);
					LogManager.d("lockScreen:delete!>>>>>>>" + ac.getClass());
					break;
				}
			}
		}

		// if (activity != null) {
		// activityStack.remove(activity);
		// if (isDestroy) {
		// activity.finish();
		// }
		// activity = null;
		// }
	}

	/**
	 * 获取栈顶的activity
	 * 
	 * @return
	 */
	public Activity currentActivity() {
		if (activityStack != null && activityStack.size() > 0) {
			Activity activity = activityStack.lastElement();
			return activity;
		} else {
			return null;
		}
	}

	/**
	 * 将activity放入栈中
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		if (isExistActivity(activity)) {
			popActivity(activity, false);
		}
		activityStack.add(activity);
		// outLog();

	}

	public boolean isExistActivityClass(Class<?> activityClass) {
		for (Activity item : activityStack) {
			if (item.getClass() == activityClass) {
				return true;
			}
		}
		return false;
	}

	public boolean isExistActivity(Activity activity) {
		for (Activity item : activityStack) {
			if (item == activity) {
				// LogManager.d("ScreenManager isExistActivity:"
				// + activity.getClass());
				return true;
			}
		}
		return false;
	}

	/**
	 * 销毁所有activity并清空栈
	 * 
	 * @param cls
	 */
	public void popAllActivity() {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			popActivity(activity, true);
		}
	}

	/**
	 * 销毁栈中某个activity上的所有activity,并从栈中移除
	 * 
	 * @param cls
	 */
	public void popAllActivityUntilOne(Class<?> cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity, true);
		}
	}

	public void outLog() {
		if (activityStack != null) {
			String s = "[";
			for (Activity item : activityStack) {
				String name = item.getClass().getName();
				s += "  " + name.substring(name.lastIndexOf(".") + 1);
			}
			s += "]";
			LogManager.d("ScreenManager " + s);
		}
	}
}