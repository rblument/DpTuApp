/*
 * DPTu: Dynamic Programming Tutor
 *
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 *
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.view.act;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import edu.regis.dptu.dao.AccountDAO;
import edu.regis.dptu.dao.StudentModelDAO;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.User;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.SvcFacade;
import edu.regis.dptu.svc.TutorReply;
import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.SplashFrame;

/**
 * An (MVC) controller handling a GUI gesture representing a user's request to login to the tutor
 * via the WelcomePanel.
 *
 * <p>If successful, a trial will be started or resumed for the student via launch session.
 *
 * @author rickb
 */
public class SignInAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(SignInAction.class);

    private static final SignInAction SINGLETON;

    static {
        SINGLETON = new SignInAction();
    }

    public static SignInAction instance() {
        return SINGLETON;
    }

    private SignInAction() {
        super(ResourceMgr.instance().string("action.signIn.name"));
        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.signIn.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Gson gson = new Gson();
        SplashFrame frame = SplashFrame.instance();
        User user = frame.getUser();

        ClientRequest request = new ClientRequest(ServerRequestType.SIGN_IN);
        request.setData(gson.toJson(user));
        TutorReply reply = SvcFacade.instance().tutorRequest(request);

        switch (reply.getStatus()) {
            case "Authenticated":
                try {
                    AccountDAO accDao = new AccountDAO();
                    Account studentAccount = accDao.retrieve(user.getUserId());
                    StudentModelDAO smDao = new StudentModelDAO();
                    StudentModel sm = smDao.retrieve(user.getUserId());
                    Student student = new Student(studentAccount);
                    student.setStudentModel(sm);
                    SplashFrame.instance().setStudent(student);

                    if (student.getStudentModel().getSessions().isEmpty()) {
                        frame.setIsFirstLogin(true);
                    }
                    String name = studentAccount.getFirstName();
                    SplashFrame.instance().initializeDashboard(name);
                } catch (ObjNotFoundException e) {
                    log.error("No account found", e);
                } catch (NonRecoverableException e) {
                    log.error("Non-recoverable error during sign-in", e);
                }

                break;
            case "InvalidPassword":
                JOptionPane.showMessageDialog(
                        null, ResourceMgr.instance().string("auth.error.invalidPassword"));
                break;
            case "UnknownUser":
                JOptionPane.showMessageDialog(
                        null, ResourceMgr.instance().string("auth.error.unknownUser"));
            default:
                JOptionPane.showMessageDialog(
                        null, ResourceMgr.instance().string("error.unknownTryAgain"));
        }
    }
}
