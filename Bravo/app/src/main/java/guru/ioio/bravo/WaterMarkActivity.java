package guru.ioio.bravo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import guru.ioio.bravo.databinding.ActivityWaterMarkBinding;
import guru.ioio.bravo.utils.WaterMarkHelper;

/**
 * Created by daniel on 6/5/17.
 * for water mark
 */

public class WaterMarkActivity extends Activity {
    public ObservableField<String> text = new ObservableField<>();
    private ActivityWaterMarkBinding mBinding;
    private AsyncTask<?, ?, ?> mTask;
    private WaterMarkHelper mHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_water_mark);
        mBinding.setPresenter(this);
        mHelper = new WaterMarkHelper();
    }

    public boolean refresh() {
        if (mTask == null) {
            mTask = new AsyncTask<Object, Object, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Object... params) {
                    mHelper.initialize(getApplicationContext());
                    Bitmap bitmap = mHelper.createWaterMark(getApplicationContext(), "wm_0.png", "92138470912837409128374");
                    mHelper.release(getApplicationContext());
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    if (result != null) {
                        mBinding.image.setImageBitmap(result);
                    }
                    mTask = null;
                }
            };
            mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return true;
    }

}
