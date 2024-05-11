package org.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Route("")
@PermitAll
@CssImport("styles.css")
@Component
public class MainView extends HorizontalLayout {
//  @Autowired
//  private UserRepository userRepository;

  public MainView() {
    setSizeFull();
    setSpacing(false);

    var left = new VerticalLayout();
    left.setPadding(false);
    left.setWidth(null);
    left.setClassName("left");
    left.setAlignItems(Alignment.CENTER);
    left.setJustifyContentMode(JustifyContentMode.CENTER);

    var right = new VerticalLayout();
    right.setPadding(false);
    right.setWidth(null);
    right.setClassName("right");
    right.setAlignItems(Alignment.CENTER);
    right.setJustifyContentMode(JustifyContentMode.CENTER);

    var clients = new Button("CLIENTS");
    clients.setClassName("clients");


    clients.addClickListener(e -> {
      left.setClassName("left-hidden");
    });

    var workers = new Button("WORKERS");
    workers.setClassName("workers");

    workers.addClickListener(e -> {
      right.setClassName("right-hidden");
    });

    H1 logInAsAClient = new H1("Log in as a client");
    right.add(logInAsAClient, clients);
    left.add(new H1("Log in as a professional"), workers);

    var rightCounter = new Counter();
    var email = new EmailField("Email");
    var password = new PasswordField("Password");

    right.getElement().addEventListener("transitionend", e -> {
      if (e.getSource().getClassList().contains("right-hidden")) {
        if (rightCounter.get() == 3) {
          remove(right);
          left.addComponentAtIndex(1, email);
          left.addComponentAtIndex(2, password);
          workers.setText("LOG IN");
          workers.addClickListener(we -> {
            if (!email.isInvalid() && !password.isInvalid()) {
              var user = new User(email.getValue(), password.getValue());
              //SUCCESSFUL LOGIN
              getElement().executeJs("window.location.href = $0", "http://localhost:8081");
            }
          });
        }
        else rightCounter.increment();
      }
    });

    var leftCounter = new Counter();
    left.getElement().addEventListener("transitionend", e -> {
      if (e.getSource().getClassList().contains("left-hidden")) {
        if (leftCounter.get() == 3) {
          remove(left);
          right.addComponentAtIndex(1, email);
          right.addComponentAtIndex(2, password);
        }
        else leftCounter.increment();
      }
    });
    add(left, right);
  }
}