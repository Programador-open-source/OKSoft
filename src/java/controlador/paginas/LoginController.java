/* 
 Description:
 ZK Essentials
 History:
 Created by dennis

 Copyright (C) 2012 Potix Corporation. All Rights Reserved.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.UsersJpaController;
import entidades.Usergrupo;
import entidades.Users;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

public class LoginController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    //wire components
    @Wire
    Textbox account;
    @Wire
    Textbox password;
    @Wire
    Label message;

    //services
    Autenticacao authService = new AutenticacaoImpl();

    @Listen("onClick=#login; onOK=#login")
    public void doLogin() {
        String nm = account.getValue();
        String pd = password.getValue();

        if (!authService.login(nm, pd)) {
            message.setVisible(true);
            message.setValue("utilizador ou senha incorrecta.");
            return;
        }

        UserCredential cre = authService.getUserCredential();
        Users actual = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
        
        List<Usergrupo> grupos = actual.getUsergrupoList();
        for (Usergrupo grupo : grupos) {
            if (grupo.getUsergrupoPK().getIdGrupo().equals("Administrador")) {
                message.setVisible(true);
                message.setValue("Bem-vindo, " + new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount()).getNome());
                message.setSclass("alert alert-success fade in");
                Executions.sendRedirect("/Paginas/admin/gestao.zul");
                
            }

        }
        
    }
}
