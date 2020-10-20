/* 
 Description:
 ZK Essentials
 History:
 Created by dennis

 Copyright (C) 2012 Potix Corporation. All Rights Reserved.
 */
package servicos;

import conexao.JPA;
import controladores.entidades.UsersJpaController;

import entidades.Users;
import java.io.Serializable;
import java.util.List;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class AutenticacaoImpl implements Autenticacao, Serializable {

    private static final long serialVersionUID = 1L;

    public UserCredential getUserCredential() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute("userCredential");
        if (cre == null) {
            cre = new UserCredential();//new a anonymous user and set to session
            sess.setAttribute("userCredential", cre);

        }
        return cre;
    }

    public boolean login(String nm, String pd) {
        Users user = new UsersJpaController(new JPA().getEmf()).findUsers(nm);

        if (user == null || !user.getPasword().equals(pd)) {
            return false;
        }

        Session sess = Sessions.getCurrent();
        UserCredential cre = new UserCredential(user.getUtilizador(), user.getNome());
        sess.setAttribute("userCredential", cre);
        return true;

            //            if (cre.isAnonymous()) {
//                return false;
//            }
    }

    public void logout() {
        Session sess = Sessions.getCurrent();
        sess.removeAttribute("userCredential");
    }
}
