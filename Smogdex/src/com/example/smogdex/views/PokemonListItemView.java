package com.example.smogdex.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smogdex.PokemonListItem;
import com.example.smogdex.R;

public class PokemonListItemView extends FrameLayout {

	private static final String TAG = PokemonListItemView.class.getSimpleName();

	private ImageView mImage;
	private TextView mName;
	private TextView mNumber;

	public static final Options ICON_DECODE_OPTIONS;
	static {
		ICON_DECODE_OPTIONS = new BitmapFactory.Options();
		ICON_DECODE_OPTIONS.inScaled = false;
		ICON_DECODE_OPTIONS.inDither = false;
		ICON_DECODE_OPTIONS.inPreferredConfig = Bitmap.Config.ARGB_8888;
	}

	public PokemonListItemView(Context context) {
		super(context);
		inflate(context);
	}

	public PokemonListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context);
	}

	public PokemonListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context);
	}

	private void inflate(Context context) {
		((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pokemon_list_item, this, true);

		// this is not called if inflated manually
		onFinishInflate();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mImage = (ImageView) findViewById(R.id.icon);
		mName = (TextView) findViewById(R.id.name);
		mNumber = (TextView) findViewById(R.id.number);
	}

	public void setupForItem(PokemonListItem item, Context context, int scale) {
		Bitmap image = BitmapFactory.decodeResource(context.getResources(), item.mImage, ICON_DECODE_OPTIONS);
		Bitmap scaledImage = Bitmap.createScaledBitmap(image, image.getWidth() * scale, image.getHeight() * scale, false);

		mImage.setImageBitmap(scaledImage);
		mName.setText(item.mName);
		mNumber.setText("#" + String.format("%03d", item.mNumber));
	}

}
