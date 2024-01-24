package mc.recraftors.unruled_api.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import mc.recraftors.unruled_api.UnruledApiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * {@link MultiLineTextFieldWidget}'s scrollbar widget.
 *
 * Based off of
 * <a href="https://github.com/Tectato/BetterCommandBlockUI/blob/main/src/main/java/bettercommandblockui/main/ui/ScrollbarWidget.java">
 * Tectato's BetterCommandBlockUI widget</a>
 */
public class ScrollbarWidget extends ClickableWidget {
    private boolean dragging = false;
    private boolean shift = false;
    private double prefMouseX = 0d;
    private double prevMouseY = 0d;
    private double pos = 0d;
    private boolean horizontal;
    private double scale;
    private int length;
    private int barLength;
    private final MultiLineTextFieldWidget textField;

    public ScrollbarWidget(int x, int y, int width, int height, Text message, MultiLineTextFieldWidget textField, boolean horizontal) {
        super(x, y, width, height, message);
        this.textField = textField;
        this.horizontal = horizontal;
        this.scale = 1d;
        this.length = horizontal ? width : height;
        this.barLength = (int) (length / scale);
    }

    public void setScale(double scale) {
        this.scale = Math.max(scale, 1);
        barLength = (int)(length / scale);
    }

    public void updatePos(double newPos) {
        pos = Math.max(Math.min(newPos, 1), 0);
    }

    private boolean checkHovered(double mouseX, double mouseY){
        if(horizontal){
            return mouseX >= this.getX() + pos * (length-barLength) && mouseY >= this.getY() && mouseX < this.getX() + pos * (length-barLength) + barLength && mouseY < this.getY() + this.height;
        } else {
            return mouseX >= this.getX() && mouseY >= this.getY() + pos * (length-barLength) && mouseX < this.getX() + this.width && mouseY < this.getY() + pos * (length-barLength) + barLength;
        }
    }

    private void renderFrame(DrawContext context) {
        if (horizontal) {
            RenderSystem.setShaderTexture(0, UnruledApiClient.SCROLLBAR_HORIZONTAL);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            /*drawTexture(context, getX(), getY(), 0, 0, width / 2, height, 256, 30);
            drawTexture(context, getX() + width / 2, getY(), 256 - width / 2f, 0, width / 2, height, 256, 30);*/
        } else {
            RenderSystem.setShaderTexture(0, UnruledApiClient.SCROLLBAR_VERTICAL);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            /*drawTexture(context, UnruledApiClient.SCROLLBAR_HORIZONTAL, getX(), getY(), getY() + height / 2, 0, 256 - height / 2, width, height, 30, 256);
            drawTexture(context, getX(), getY(), 0, 0, width, height / 2, 30, 256);
            drawTexture(context, getX(), getY() + height / 2, 0, 256 - height / 2f, width, height / 2, 30, 256);*/
        }
    }

    private void renderSlider(DrawContext context, int mouseX, int mouseY, float delta) {
        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int i = this.hovered || this.dragging ? 10 : 0;

        if (horizontal) {
            RenderSystem.setShaderTexture(0, UnruledApiClient.SCROLLBAR_HORIZONTAL);

            /*drawTexture(context, getX() + (int)(pos * (length - barLength)), getY(), 0, 10f + i, barLength / 2, height, 256, 30);
            drawTexture(context, getX() + (int)(pos * (length - barLength)) + barLength / 2, getY(), 256 - barLength / 2f, 10f + i, barLength / 2, height, 256, 30);*/
        } else {
            RenderSystem.setShaderTexture(0, UnruledApiClient.SCROLLBAR_VERTICAL);

            /*drawTexture(context, getX(), getY() + (int)(pos * (length - barLength)), 10f + i, 0, width, barLength / 2, 30, 256);
            drawTexture(context, getX(), getY(), (int)(pos * (length - barLength)) + barLength / 2, 10f + i, 256 - barLength / 2f, width, barLength / 2, 30, 256);*/
        }
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        if (!visible) return false;
        return checkHovered(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isValidClickButton(button) && clicked(mouseX, mouseY)) {
            dragging = true;
            playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onClick(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.isValidClickButton(button)) {
            this.dragging = false;
            this.onRelease(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.visible) return;
        this.hovered = checkHovered(mouseX, mouseY);

        this.renderFrame(context);
        this.renderSlider(context, mouseX, mouseY, delta);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (!visible) return;
        dragging = true;
        prefMouseX = mouseX;
        prevMouseY = mouseY;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (!visible) return;
        dragging = false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        if (dragging) {
            double distX = mouseX - prevMouseY;
            double distY = mouseY - prevMouseY;
            prefMouseX = mouseX;
            prevMouseY = mouseY;

            onDrag(mouseX, mouseY, distX, distY);
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (dragging) {
            if (horizontal) {
                pos = Math.min(Math.max(pos + deltaX / (length - barLength), 0), 1);
                textField.setHorizontalOffset(pos);
            } else {
                pos = Math.min(Math.max(pos + deltaY / (length - barLength), 0), 1);
                textField.setScroll(pos);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (pos == 0 || pos == 1) return false;
        pos = Math.max(Math.min(((double) barLength / length) * amount, 1), 0);
        if (horizontal) textField.setHorizontalOffset(pos);
        else textField.setScroll(pos);
        return true;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
