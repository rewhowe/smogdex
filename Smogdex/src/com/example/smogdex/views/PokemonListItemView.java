package com.example.smogdex.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smogdex.PokemonListItem;
import com.example.smogdex.R;

public class PokemonListItemView extends FrameLayout {

	private ImageView mImage;
	private TextView mName;
	private TextView mNumber;

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
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mImage = (ImageView) findViewById(R.id.icon);
		mName = (TextView) findViewById(R.id.name);
		mNumber = (TextView) findViewById(R.id.number);
	}

	public void setupForItem(PokemonListItem item, Bitmap image) {
		Bitmap scaledImage = Bitmap.createScaledBitmap(image, image.getWidth() * 2, image.getHeight() * 2, false);

		mImage.setImageBitmap(scaledImage);
		mName.setText(item.name);
		mNumber.setText("#" + String.format("%03d", item.number));
	}

}
