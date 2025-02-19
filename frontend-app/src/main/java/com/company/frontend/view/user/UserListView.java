package com.company.frontend.view.user;

import com.company.frontend.entity.User;
import com.company.frontend.view.main.MainView;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "users", layout = MainView.class)
@ViewController("User.list")
@ViewDescriptor("user-list-view.xml")
@LookupComponent("usersDataGrid")
@DialogMode(width = "64em")
public class UserListView extends StandardListView<User> {

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private FileStorage fileStorage;

    @Supply(to = "usersDataGrid.picture", subject = "renderer")
    private Renderer<User> usersDataGridPictureRenderer() {
        return new ComponentRenderer<>(user -> {
            FileRef fileRef = user.getPicture();
            if (fileRef != null) {
                Image image = uiComponents.create(Image.class);
                image.setWidth("30px");
                image.setHeight("30px");
                StreamResource streamResource = new StreamResource(
                        fileRef.getFileName(),
                        () -> fileStorage.openStream(fileRef));
                image.setSrc(streamResource);
                image.setClassName("user-picture");

                return image;
            } else {
                return null;
            }
        });
    }
}