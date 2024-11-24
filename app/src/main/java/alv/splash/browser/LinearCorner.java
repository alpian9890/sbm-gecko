package alv.splash.browser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

public class LinearCorner extends LinearLayout {
    private Path path;
    private float cornerRadius = 30f; // ukuran sudut dalam dp

    public LinearCorner(Context context) {
        super(context);
        init();
    }

    public LinearCorner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearCorner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.reset();
        path.addRoundRect(0, 0, w, h, new float[] {cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0, 0, 0, 0}, Path.Direction.CW);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    @Override
    public void setOutlineProvider(ViewOutlineProvider provider) {
        super.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
    }
}