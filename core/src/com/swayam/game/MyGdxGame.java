package com.swayam.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	Texture background;
	Texture toptube;
	Texture gameover;
	BitmapFont font;
	Texture bottomtube;
	int i=0;
	Texture[] birds;
	Circle birdcircle;
	int flap_state=0;
	float bird_y=0;
	int game_state=0;
	float velocity=0;
	float gravity=3;
	float gap=500;
	float maxoffset;
	Random randomnumber;
	Rectangle[] toptube_rectangle;
	Rectangle[] bottomtube_rectangle;

	float tubevelocity=4;
	int numberoftubes=4;
	float[] tubex=new float[numberoftubes];
	float[] tubeoffset=new float[numberoftubes];
	float dis;
	int score=0;
	int scoringtube=0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover=new Texture("gameover.jpg");
		birdcircle=new Circle();
//		shapeRenderer=new ShapeRenderer();
		toptube_rectangle=new Rectangle[numberoftubes];
		bottomtube_rectangle=new Rectangle[numberoftubes];
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		toptube=new Texture("toptube.png");
		bottomtube=new Texture("bottomtube.png");
		maxoffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomnumber=new Random();
		dis=Gdx.graphics.getWidth()*0.35f;
		startgame();

	}

	public void startgame()
	{
		bird_y=Gdx.graphics.getWidth()/2-birds[0].getWidth()/2;

		for(int i=0; i<numberoftubes; i++){
			tubeoffset[i]=(randomnumber.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubex[i]=(Gdx.graphics.getWidth()/2+Gdx.graphics.getWidth()/2-toptube.getWidth()/2)+i*dis;
			toptube_rectangle[i]=new Rectangle();
			bottomtube_rectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

//		if(Gdx.input.justTouched())
//		{
//			game_state=1;
//		}



		if(game_state==1) {

			if(Gdx.input.justTouched())
			{
//				sudden impulse upward
				velocity=-30;
			}

			if(tubex[scoringtube]<Gdx.graphics.getWidth()/2)
			{
				score++;
				scoringtube=(scoringtube+1)%numberoftubes;
			}
			for(int i=0; i<numberoftubes; i++) {
				if(tubex[i]<-toptube.getWidth())
				{
					tubex[i]+=numberoftubes*dis;
					tubeoffset[i]=(randomnumber.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
				}
				else {
					tubex[i] -= tubevelocity;
				}
				batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);

				toptube_rectangle[i]=new Rectangle(tubex[i], Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i], toptube.getWidth(), toptube.getHeight());
				bottomtube_rectangle[i]=new Rectangle(tubex[i], Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight()+tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());
			}
			if(bird_y>0)
			{
//				creating gravity like system
				velocity+=2;
				bird_y-=velocity;
			}
			else
			{
				game_state=2;
			}
		}
		else if(game_state==0)
		{
			if(Gdx.input.justTouched())
			{
				game_state=1;
			}
		}
		else if(game_state==2)
		{
			batch.draw(gameover, Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/2-gameover.getHeight()/2);

			if(Gdx.input.justTouched())
			{
				game_state=1;
				startgame();
				score=0;
				scoringtube=0;
				velocity=0;
			}
		}

		if (flap_state == 0) {
			i++;
			if (i > 3) {
				i = 0;
				flap_state = 1;
			}
		} else {
			i++;
			if (i > 3) {
				i = 0;
				flap_state = 0;
			}
		}

		batch.draw(birds[flap_state], Gdx.graphics.getWidth()/2 - birds[flap_state].getWidth() / 2, bird_y);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);

		birdcircle.set(Gdx.graphics.getWidth()/2, bird_y+birds[flap_state].getHeight()/2, birds[flap_state].getWidth()/2);
//		shapeRenderer.circle(birdcircle.x, birdcircle.y, birdcircle.radius);

		for(int i=0; i<numberoftubes; i++){

//			shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i], toptube.getWidth(), toptube.getHeight());
//			shapeRenderer.rect(tubex[i], Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight()+tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());

//			if(Intersector.overlaps(birdcircle, toptube_rectangle[i]) || Intersector.overlaps(birdcircle, bottomtube_rectangle[i]))
//			{
//				Gdx.app.log("Collision", "Yes");
//			}
			try
			{
				if (Intersector.overlaps(birdcircle, toptube_rectangle[i]) || Intersector.overlaps(birdcircle, bottomtube_rectangle[i])) {

					Gdx.app.log("Collision", "Yes");
					game_state=2;

				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}

//		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		birds.dispose();

	}
}
