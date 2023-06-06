package com.maquetech.application.components.user;

import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.helpers.EnvironmentHelper;
import com.maquetech.application.helpers.LabelHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.StringHelper;
import com.maquetech.application.models.user.CourseModel;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.course.CourseService;
import com.maquetech.application.services.user.UserService;
import com.maquetech.application.services.user.professor.ProfessorCodeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

public class UserRegistrationComponent extends Dialog {

    @Value("${root.user.code}")
    private String adminCode;

    private Binder<UserModel> binder;
    private final UserService userService;
    private final CourseService courseService;
    private final ProfessorCodeService professorCodeService;
    private final VerticalLayout vlContent = new VerticalLayout();
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final UserTermsComponent userTermsComponent = new UserTermsComponent();
    private ComboBox<CourseModel> course;
    private TextField specialCode;

    public UserRegistrationComponent(UserService userService, CourseService courseService, ProfessorCodeService professorCodeService, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.userService = userService;
        this.courseService = courseService;
        this.professorCodeService = professorCodeService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;

        init();
    }

    private void init() {
        setSizeFull();

        vlContent.setSizeFull();
        vlContent.setAlignItems(FlexComponent.Alignment.CENTER);

        add(vlContent);
    }

    private void initFields() {
        binder = new Binder<>();

        var username = new TextField("Login");
        username.setAllowedCharPattern("[A-Za-z0-9]");
        username.setRequired(true);
        binder.forField(username)
                .withValidator(input -> input.length() > 5, "Login deve conter mais que 5 caracteres!")
                .bind(UserModel::getUsername, UserModel::setUsername);

        var name = new TextField("Nome");
        name.setRequired(true);
        binder.forField(name)
                .withValidator(input -> input.length() > 4, "Nome deve conter mais que 5 caracteres!")
                .bind(UserModel::getName, UserModel::setName);

        var password = new PasswordField("Senha");
        password.setRequired(true);
        password.setAllowedCharPattern("[A-Za-z0-9]");
        password.setHelperText("Apenas letras e numeros são permitidos.");
        binder.forField(password)
                .withValidator(input -> input.length() >= 6, "Senha precisa ter no minimo 6 caracteres")
                .withValidator(input -> input.length() <= 12, "Senha precisa ter no máximo 12 caracteres")
                .asRequired()
                .bind(UserModel::getPassword, UserModel::setPassword);

        var mail = new EmailField("E-mail");
        mail.setRequired(true);
        binder.forField(mail)
                .asRequired()
                .bind(UserModel::getMail, UserModel::setMail);

        var cpf = new TextField("CPF");
        cpf.setAllowedCharPattern("[0-9]");
        cpf.setRequired(true);
        binder.forField(cpf)
                .withValidator(StringHelper::validateCPF, "CPF inválido!")
                .asRequired()
                .bind(UserModel::getCpf, UserModel::setCpf);

        var phone = new TextField("Telefone");
        phone.setPattern("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$");
        phone.setAllowedCharPattern("[0-9]");
        phone.setMinLength(10);
        phone.setMaxLength(11);
        phone.setRequired(true);
        binder.forField(phone)
                .asRequired()
                .bind(UserModel::getPhone, UserModel::setPhone);

        specialCode = new TextField("Código professor");
        specialCode.setRequired(true);
        specialCode.setVisible(false);

        course = new ComboBox<>("Curso");
        course.setRequired(true);
        course.setItems(this.courseService.getList());
        course.setItemLabelGenerator(CourseModel::getName);

        ComboBox<UserTypeEnum> typeField = new ComboBox<>("Tipo usuário");
        typeField.setItems(Arrays.asList(UserTypeEnum.LEVEL_1, UserTypeEnum.LEVEL_2, UserTypeEnum.LEVEL_3));
        typeField.setItemLabelGenerator(UserTypeEnum::getDescription);
        typeField.setValue(UserTypeEnum.LEVEL_1);
        typeField.addValueChangeListener((event) -> {
            boolean isAdmin = event.getValue().equals(UserTypeEnum.LEVEL_3);
            boolean isProfessorOrAdmin = !event.getValue().equals(UserTypeEnum.LEVEL_1);

            if (isAdmin) {
                specialCode.setLabel("Código admin");
            } else {
                specialCode.setLabel("Código professor");
            }

            specialCode.setVisible(isProfessorOrAdmin);
            course.setVisible(!isProfessorOrAdmin);

            if (isProfessorOrAdmin) {
                course.setValue(null);
            }
        });
        binder.forField(typeField)
                .asRequired()
                .bind(UserModel::getType, UserModel::setType);

        var cancel = new Button("Cancelar");
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addClickListener((event) -> this.close());

        var edit = new Button("Cadastrar");
        edit.addClickListener(event -> register());

        var layout = new FormLayout();
        layout.add(username, password, name, mail, cpf, phone, typeField, specialCode, course);
        layout.setColspan(name, 2);
        layout.setColspan(mail, 2);

        vlContent.removeAll();
        vlContent.add(layout);
        getFooter().removeAll();
        getFooter().add(userTermsComponent, cancel, edit);
    }

    private void register() {
        try {
            if (!userTermsComponent.isAceepted()) {
                throw new IllegalArgumentException("Aceite os termos de privacidade antes de prosseguir!");
            }

            var user = new UserModel();
            binder.writeBean(user);

            if (this.userService.hasByUsername(user.getUsername())) {
                throw new Exception("Login " + user.getUsername() + " já está em uso!");
            }

            if (user.isStudent() && course.getValue() == null) {
                throw new Exception("Informe um curso para finalizar o cadastro!");
            }

            if (user.isStudent()) {
                user.setCourse(course.getValue());
            }

            if (user.isProfessor()) {
                String professorCode = this.specialCode.getValue();
                if (StringUtils.isBlank(professorCode)) throw new Exception("Informe um código válido de professor para finalizar o cadastro!");

                if (!this.professorCodeService.isValidCode(professorCode)) {
                    throw new Exception("Código inválido!");
                }

                this.professorCodeService.invalidateCode(professorCode);
            } else if (user.isAdmin()) {
                String professorCode = this.specialCode.getValue();
                if (!EnvironmentHelper.getAdminCode().equals(professorCode)) {
                    throw new Exception("Código admin inválido!");
                }
            }

            String password = user.getPassword();
            String saltPassword = BCrypt.gensalt();
            String bcryptPassword = BCrypt.hashpw(password, saltPassword);
            user.setPassword(bcryptPassword);

            this.userService.save(user, inMemoryUserDetailsManager);
            NotificationHelper.success("Cadastro realizado com sucesso!");
            this.close();
        } catch (ValidationException ex) {
            ex.printStackTrace();
            NotificationHelper.error("Alguns campos não foram preenchidos corretamente!");
        } catch (Exception e) {
            e.printStackTrace();
            NotificationHelper.error(e.getMessage());
        }
    }

    public void open() {
        initFields();
        super.open();
    }
}
