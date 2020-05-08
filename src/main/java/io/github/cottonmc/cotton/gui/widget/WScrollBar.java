package io.github.cottonmc.cotton.gui.widget;

import io.github.cottonmc.cotton.gui.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.minecraft.client.util.math.MatrixStack;

public class WScrollBar extends WAbstractSlider {
	protected int window = 16;

	/**
	 * Constructs a horizontal scroll bar.
	 */
	public WScrollBar() {
		super(0, 100, Axis.HORIZONTAL);
	}

	/**
	 * Constructs a scroll bar with a custom axis.
	 *
	 * @param axis the axis
	 */
	public WScrollBar(Axis axis) {
		super(0, 100, axis);
	}

	@Override
	protected int getThumbWidth() {
		return window;
	}

	@Override
	protected boolean isMouseInsideBounds(int x, int y) {
		return axis == Axis.HORIZONTAL
				? (x >= getHandlePosition() + 1 && x <= getHandlePosition() + getHandleSize())
				: (y >= getHandlePosition() + 1 && y <= getHandlePosition() + getHandleSize());
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		if (LibGuiClient.config.darkMode) {
			ScreenDrawing.drawBeveledPanel(x, y, width, height, 0xFF_212121, 0xFF_2F2F2F, 0xFF_5D5D5D);
		} else {
			ScreenDrawing.drawBeveledPanel(x, y, width, height, 0xFF_373737, 0xFF_8B8B8B, 0xFF_FFFFFF);
		}
		if (getMaxValue()<=0) return;

		// Handle colors
		int top, middle, bottom;

		if (dragging) {
			if (LibGuiClient.config.darkMode) {
				top = 0xFF_6C6C6C;
				middle = 0xFF_2F2F2F;
				bottom = 0xFF_212121;
			} else {
				top = 0xFF_FFFFFF;
				middle = 0xFF_8B8B8B;
				bottom = 0xFF_555555;
			}
		} else if (isWithinBounds(mouseX, mouseY)) {
			if (LibGuiClient.config.darkMode) {
				top = 0xFF_5F6A9D;
				middle = 0xFF_323F6E;
				bottom = 0xFF_0B204A;
			} else {
				top = 0xFF_CFD0F7;
				middle = 0xFF_8791C7;
				bottom = 0xFF_343E75;
			}
		} else {
			if (LibGuiClient.config.darkMode) {
				top = 0xFF_6C6C6C;
				middle = 0xFF_414141;
				bottom = 0xFF_212121;
			} else {
				top = 0xFF_FFFFFF;
				middle = 0xFF_C6C6C6;
				bottom = 0xFF_555555;
			}
		}

		if (axis==Axis.HORIZONTAL) {
			ScreenDrawing.drawBeveledPanel(x+1+getHandlePosition(), y+1, getHandleSize(), height-2, top, middle, bottom);
		} else {
			ScreenDrawing.drawBeveledPanel(x+1, y+1+getHandlePosition(), width-2, getHandleSize(), top, middle, bottom);
		}
	}
	
	/**
	 * Gets the on-axis size of the scrollbar handle in gui pixels 
	 */
	public int getHandleSize() {
		float percentage = (window>=getMaxValue()) ? 1f : window / (float)getMaxValue();
		int bar = (axis==Axis.HORIZONTAL) ? width-2 : height-2;
		int result = (int)(percentage*bar);
		if (result<6) result = 6;
		return result;
	}
	
	/**
	 * Gets the number of pixels the scrollbar handle is able to move along its track from one end to the other.
	 */
	public int getMovableDistance() {
		int bar = (axis==Axis.HORIZONTAL) ? width-2 : height-2;
		return bar-getHandleSize();
	}
	
	public int getHandlePosition() {
		float percent = value / (float)Math.max(getMaxValue()-window, 1);
		return (int)(percent * getMovableDistance());
	}
	
	/**
	 * Gets the maximum scroll value achievable; this will typically be the maximum value minus the
	 * window size
	 */
	public int getMaxScrollValue() {
		return getMaxValue() - window;
	}

	public void setValue(int value) {
		super.setValue(value);
		checkValue();
	}

	public void setMaxValue(int max) {
		super.setMaxValue(max);
		checkValue();
	}

	public int getWindow() {
		return window;
	}

	public WScrollBar setWindow(int window) {
		this.window = window;
		return this;
	}

	/**
	 * Checks that the current value is in the correct range
	 * and adjusts it if needed.
	 */
	protected void checkValue() {
		if (this.value>getMaxValue()-window) {
			this.value = getMaxValue()-window;
		}
		if (this.value<0) this.value = 0;
	}
}
