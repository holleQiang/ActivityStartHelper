package com.zhangqiang.activitystart;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zhangqiang.holderfragment.HolderFragment;
import com.zhangqiang.holderfragment.SimpleLifecycleCallback;

public class ActivityStartHelper {

    private static final RequestCodeGetter mRequestCodeGetter = new RequestCodeGetter();

    public static void startActivityForResult(FragmentActivity activity,
                                              final Intent intent,
                                              final Callback callback) {
        startActivityWithHolderFragment(HolderFragment.forActivity(activity),intent,callback);
    }

    public static void startActivityForResult(Fragment fragment,
                                              final Intent intent,
                                              final Callback callback) {

        startActivityWithHolderFragment(HolderFragment.forFragment(fragment), intent, callback);
    }


    private static void startActivityWithHolderFragment(final HolderFragment holderFragment,
                                                        final Intent intent,
                                                        final Callback callback) {
        final int requestCode = mRequestCodeGetter.getRequestCode();
        holderFragment.registerActivityResultCallback(new HolderFragment.OnActivityResultCallback() {
            @Override
            public void onActivityResult(int requestCode1, int resultCode, Intent data) {
                if (callback != null && requestCode == requestCode1) {
                    holderFragment.unregisterActivityResultCallback(this);
                    callback.onActivityResult(resultCode, data);
                }
            }
        });
        if (holderFragment.getActivity() == null) {
            holderFragment.registerLifecycleCallback(new SimpleLifecycleCallback() {
                @Override
                public void onAttach(Context context) {
                    super.onAttach(context);
                    holderFragment.unregisterLifecycleCallback(this);

                    holderFragment.startActivityForResult(intent, requestCode);
                }
            });
        } else {
            holderFragment.startActivityForResult(intent, requestCode);
        }
    }

    public interface Callback {

        void onActivityResult(int resultCode, Intent data);
    }


    private static class RequestCodeGetter {

        private int mRequestCode = 0;

        private int getRequestCode() {
            final int targetRequestCode = mRequestCode;
            if (mRequestCode == Integer.MAX_VALUE) {
                mRequestCode = 0;
            } else {
                mRequestCode++;
            }
            return targetRequestCode;
        }

    }

}
