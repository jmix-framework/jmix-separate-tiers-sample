package com.company.frontend.view.myonboarding;


import com.company.frontend.entity.User;
import com.company.frontend.entity.UserStep;
import com.company.frontend.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Sort;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Route(value = "MyOnboardingView", layout = MainView.class)
@ViewController("MyOnboardingView")
@ViewDescriptor("my-onboarding-view.xml")
public class MyOnboardingView extends StandardView {

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @ViewComponent
    private CollectionLoader<UserStep> userStepsDl;

    @Autowired
    private UiComponents uiComponents;

    @ViewComponent
    private CollectionContainer<UserStep> userStepsDc;

    @ViewComponent
    private Span completedStepsLabel;

    @ViewComponent
    private Span overdueStepsLabel;

    @ViewComponent
    private Span totalStepsLabel;

    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private DataManager dataManager;

    @Supply(to = "userStepsDataGrid.completed", subject = "renderer")
    private Renderer<UserStep> userStepsDataGridCompletedRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Checkbox checkbox = uiComponents.create(Checkbox.class);
            checkbox.setValue(userStep.getCompletedDate() != null);
            checkbox.addValueChangeListener(e -> {
                if (userStep.getCompletedDate() == null) {
                    userStep.setCompletedDate(LocalDate.now());
                } else {
                    userStep.setCompletedDate(null);
                }
            });
            return checkbox;
        });
    }

    private void updateLabels() {
        totalStepsLabel.setText("Total steps: " + userStepsDc.getItems().size());

        long completedCount = userStepsDc.getItems().stream()
                .filter(us -> us.getCompletedDate() != null)
                .count();
        completedStepsLabel.setText("Completed steps: " + completedCount);

        long overdueCount = userStepsDc.getItems().stream()
                .filter(us -> isOverdue(us))
                .count();
        overdueStepsLabel.setText("Overdue steps: " + overdueCount);
    }

    private boolean isOverdue(UserStep us) {
        return us.getCompletedDate() == null
                && us.getDueDate() != null
                && us.getDueDate().isBefore(LocalDate.now());
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        final User user = (User) currentAuthentication.getUser();
        userStepsDl.setParameter("userId", user.getId());
        userStepsDl.load();

        updateLabels();
    }

    // tag::sorting[]
    @Install(to = "userStepsDl", target = Target.DATA_LOADER)
    private List<UserStep> userStepsDlLoadDelegate(final LoadContext<UserStep> loadContext) {
        loadContext.getQuery().setSort(Sort.by("sortValue"));
        return dataManager.loadList(loadContext);
    }
    // end::sorting[]

    @Subscribe(id = "userStepsDc", target = Target.DATA_CONTAINER)
    public void onUserStepsDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<UserStep> event) {
        updateLabels();
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.save();
        close(StandardOutcome.SAVE);
    }

    @Subscribe("discardButton")
    public void onDiscardButtonClick(final ClickEvent<JmixButton> event) {
        close(StandardOutcome.DISCARD);
    }

    @Install(to = "userStepsDataGrid.dueDate", subject = "partNameGenerator")
    private String userStepsDataGridDueDatePartNameGenerator(final UserStep userStep) {
        return isOverdue(userStep) ? "overdue-step" : null;
    }
}