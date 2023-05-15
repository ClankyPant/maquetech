package com.example.application.views.user;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.entities.course.CourseEntity;
import com.example.application.entities.user.UserEntity;
import com.example.application.enums.user.UserTypeEnum;
import com.example.application.helpers.NotificationHelper;
import com.example.application.services.course.CourseService;
import com.example.application.services.user.professor.ProfessorCodeService;
import com.example.application.services.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@AnonymousAllowed
@PageTitle("Cadastro de usuário")
@Route(value = "user-registration")
public class UserRegistrationView extends MaqueVerticalLayout {

    @Value("${root.user.code}")
    private String adminCode;

    private final ProfessorCodeService professorCodeService;

    private final CourseService courseService;
    private final TextField professorCodeField;
    private final ProfessorCodeService professorCodeService;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private final TextField specialUserCodeField;

    private final UserService userService;

    public UserRegistrationView(UserService userService, InMemoryUserDetailsManager inMemoryUserDetailsManager, ProfessorCodeService professorCodeService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
        this.professorCodeService = professorCodeService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setWidth("50%");

        Binder<UserEntity> binder = new Binder<>(UserEntity.class);

        FormLayout formLayout = new FormLayout();

        TextField usernameField = new TextField("Login");
        usernameField.setAllowedCharPattern("[A-Za-z0-9]");
        usernameField.setRequired(true);
        binder.forField(usernameField)
                .withValidator(username -> username.length() > 5, "Login deve conter mais que 5 caracteres!")
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        TextField nameField = new TextField("Nome");
        nameField.setRequired(true);
        binder.forField(nameField)
                .withValidator(username -> username.length() > 4, "Nome deve conter mais que 5 caracteres!")
                .bind(UserEntity::getName, UserEntity::setName);

        PasswordField passwordField = new PasswordField("Senha");
        passwordField.setRequired(true);
        passwordField.setAllowedCharPattern("[A-Za-z0-9]");
        passwordField.setHelperText("Apenas letras e numeros são permitidos.");
        binder.forField(passwordField)
                .withValidator(password -> password.length() >= 6, "Senha precisa ter no minimo 6 caracteres")
                .withValidator(password -> password.length() <= 12, "Senha precisa ter no máximo 12 caracteres")
                .asRequired()
                .bind(UserEntity::getPassword, UserEntity::setPassword);

        EmailField mailField = new EmailField("E-mail");
        mailField.setRequired(true);
        binder.forField(mailField)
                .asRequired()
                .bind(UserEntity::getMail, UserEntity::setMail);

        TextField cpfField = new TextField("CPF");
        cpfField.setAllowedCharPattern("[0-9]");
        cpfField.setRequired(true);
        binder.forField(cpfField)
                .asRequired()
                .bind(UserEntity::getCpf, UserEntity::setCpf);

        TextField phoneField = new TextField("Telefone");
        phoneField.setPattern("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$");
        phoneField.setAllowedCharPattern("[0-9]");
        phoneField.setMinLength(10);
        phoneField.setMaxLength(11);
        phoneField.setRequired(true);
        binder.forField(phoneField)
                .asRequired()
                .bind(UserEntity::getPhone, UserEntity::setPhone);

        specialUserCodeField = new TextField("Código professor");
        specialUserCodeField.setRequired(true);
        specialUserCodeField.setVisible(false);

        ComboBox<CourseEntity> courseComboBox = new ComboBox<>("Curso");
        courseComboBox.setRequired(true);
        courseComboBox.setItems(this.courseService.findAll());
        courseComboBox.setItemLabelGenerator(CourseEntity::getName);

        ComboBox<UserTypeEnum> typeField = new ComboBox<>("Tipo usuário");
        typeField.setItems(Arrays.asList(UserTypeEnum.LEVEL_1, UserTypeEnum.LEVEL_2, UserTypeEnum.LEVEL_3));
        typeField.setItemLabelGenerator(UserTypeEnum::getDescription);
        typeField.setValue(UserTypeEnum.LEVEL_1);
        typeField.addValueChangeListener((event) -> {
            boolean isAdmin = event.getValue().equals(UserTypeEnum.LEVEL_3);
            boolean isProfessorOrAdmin = !event.getValue().equals(UserTypeEnum.LEVEL_1);

            if (isAdmin) {
                specialUserCodeField.setLabel("Código admin");
            } else {
                specialUserCodeField.setLabel("Código professor");
            }

            specialUserCodeField.setVisible(isProfessorOrAdmin);
            courseComboBox.setVisible(!isProfessorOrAdmin);

            if (isProfessorOrAdmin) {
                courseComboBox.setValue(null);
            }
        });
        binder.forField(typeField)
                .asRequired()
                .bind(UserEntity::getType, UserEntity::setType);

        formLayout.add(usernameField, passwordField, nameField, mailField, cpfField, phoneField, typeField, specialUserCodeField, courseComboBox);
        formLayout.setColspan(nameField, 2);
        formLayout.setColspan(mailField, 2);

        Button btnRegister = new Button("Cadastrar-se");
        btnRegister.addClickListener((event) -> {
            try {
                UserEntity userEntity = new UserEntity();
                binder.writeBean(userEntity);

                if (this.userService.hasByUsername(userEntity.getUsername())) {
                    throw new Exception("Login " + userEntity.getUsername() + " já está em uso!");
                }

                if (userEntity.isStudent() && courseComboBox.getValue() == null) {
                    throw new Exception("Informe um curso para finalizar o cadastro!");
                }

                if (userEntity.isStudent()) {
                    userEntity.setCourse(courseComboBox.getValue());
                }

                if (userEntity.isProfessor()) {
                    String professorCode = this.specialUserCodeField.getValue();
                    if (StringUtils.isBlank(professorCode)) throw new Exception("Informe um código válido de professor para finalizar o cadastro!");

                    if (!this.professorCodeService.isValidCode(professorCode)) {
                        throw new Exception("Código inválido!");
                    }

                    this.professorCodeService.invalidateCode(professorCode);
                } else if (userEntity.isAdmin()) {
                    String professorCode = this.specialUserCodeField.getValue();
                    if (!this.adminCode.equals(professorCode)) {
                        throw new Exception("Código admin inválido!");
                    }
                }

                String password = userEntity.getPassword();
                String saltPassword = BCrypt.gensalt();
                String bcryptPassword = BCrypt.hashpw(password, saltPassword);
                userEntity.setPassword(bcryptPassword);

                this.userService.save(userEntity);
                this.inMemoryUserDetailsManager.createUser(this.userService.createUserDetail(userEntity));
                this.getUI().ifPresent(ui -> ui.navigate(UserLoginView.class));
            } catch (ValidationException ex) {
                ex.printStackTrace();
                NotificationHelper.error("Alguns campos não foram preenchidos corretamente!");
            } catch (Exception e) {
                e.printStackTrace();
                NotificationHelper.error(e.getMessage());
            }
        });

        Button btnCancel = new Button("Cancelar");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnCancel.addClickListener((event) -> {
            this.getUI().ifPresent(ui -> ui.navigate(UserLoginView.class));
        });

        HorizontalLayout hlButtons = new HorizontalLayout();
        hlButtons.add(btnRegister, btnCancel);

        vlContent.add(formLayout, hlButtons);

        add(vlContent);
    }
}
