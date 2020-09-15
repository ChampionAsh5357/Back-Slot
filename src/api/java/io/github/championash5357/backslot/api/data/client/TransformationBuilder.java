package io.github.championash5357.backslot.api.data.client;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

/**
 * This is a helper class used to generate 
 * back slot item renders for users. It can 
 * be used in conjunction with the provider 
 * {@link BackSlotTransformationProvider} to 
 * generate the renders in the assets folder.
 * */
public class TransformationBuilder {

	private ResourceLocation location;
	@Nullable
	private Vector3f translation, scale, rotation, postRotation;

	public TransformationBuilder() {}

	private TransformationBuilder(ResourceLocation location) {
		this.location = location;
	}

	/**
	 * Gets an instance of the builder for the specific item.
	 * 
	 * @param item
	 * 			The associated item
	 * @return {@link TransformationBuilder}
	 * */
	public static TransformationBuilder transformItem(Item item) {
		return new TransformationBuilder(item.getRegistryName());
	}

	/**
	 * Translates the item in the specified directions.
	 * 
	 * @param x
	 * 			X direction translation
	 * @param y
	 * 			Y direction translation
	 * @param z
	 * 			Z direction translation
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder translate(float x, float y, float z) {
		this.translation = new Vector3f(x, y, z);
		return this;
	}

	/**
	 * Translates the item in the specified directions.
	 * 
	 * @param vec3f
	 * 			A vector translation
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder translate(Vector3f vec3f) {
		this.translation = vec3f.copy();
		return this;
	}

	/**
	 * Rotates the item left in the specified directions in degrees.
	 * 
	 * @param x
	 * 			X direction rotation
	 * @param y
	 * 			Y direction rotation
	 * @param z
	 * 			Z direction rotation
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder rotate(float x, float y, float z) {
		this.rotation = new Vector3f(x, y, z);
		return this;
	}

	/**
	 * Rotates the item left in the specified directions in degrees.
	 * 
	 * @param vec3f
	 * 			A vector rotation
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder rotate(Vector3f vec3f) {
		this.rotation = vec3f.copy();
		return this;
	}

	/**
	 * Scales the item in the specified directions.
	 * 
	 * @param x
	 * 			X direction scale
	 * @param y
	 * 			Y direction scale
	 * @param z
	 * 			Z direction scale
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder scale(float x, float y, float z) {
		this.scale = new Vector3f(x, y, z);
		return this;
	}

	/**
	 * Scales the item in the specified directions.
	 * 
	 * @param vec3f
	 * 			A vector scale
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder scale(Vector3f vec3f) {
		this.scale = vec3f.copy();
		return this;
	}

	/**
	 * Rotates the item right in the specified directions in degrees.
	 * 
	 * @param x
	 * 			X direction rotation
	 * @param y
	 * 			Y direction rotation
	 * @param z
	 * 			Z direction rotation
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder postRotation(float x, float y, float z) {
		this.postRotation = new Vector3f(x, y, z);
		return this;
	}

	/**
	 * Rotates the item right in the specified directions in degrees.
	 * 
	 * @param vec3f
	 * 			A vector rotation
	 * @return An instance of the builder.
	 * */
	public TransformationBuilder postRotation(Vector3f vec3f) {
		this.postRotation = vec3f.copy();
		return this;
	}

	/**
	 * Adds the transformation to the provider to be generated.
	 * 
	 * @param consumer
	 * 			The associated consumer
	 * */
	public void build(Consumer<TransformationBuilder> consumer) {
		if(this.translation == null && this.scale == null && this.rotation == null && this.postRotation == null)
			throw new IllegalArgumentException("There were no changes made to " + location);
		consumer.accept(this);
	}


	protected ResourceLocation getItemName() {
		return location;
	}

	protected JsonElement serialize() {
		JsonObject parent = new JsonObject();
		if(this.translation != null) {
			JsonArray list = new JsonArray();
			list.add(this.translation.getX());
			list.add(this.translation.getY());
			list.add(this.translation.getZ());
			parent.add("translation", list);
		}
		if(this.rotation != null) {
			JsonArray list = new JsonArray();
			list.add(this.rotation.getX());
			list.add(this.rotation.getY());
			list.add(this.rotation.getZ());
			parent.add("rotation", list);
		}
		if(this.scale != null) {
			JsonArray list = new JsonArray();
			list.add(this.scale.getX());
			list.add(this.scale.getY());
			list.add(this.scale.getZ());
			parent.add("scale", list);
		}
		if(this.postRotation != null) {
			JsonArray list = new JsonArray();
			list.add(this.postRotation.getX());
			list.add(this.postRotation.getY());
			list.add(this.postRotation.getZ());
			parent.add("post-rotation", list);
		}
		return parent;
	}
}
