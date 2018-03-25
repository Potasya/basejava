package web;

import config.Config;
import model.ContactType;
import model.Resume;
import storage.SqlStorage;
import storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by Marisha on 23/03/2018.
 */
public class ResumeServlet extends HttpServlet{
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String DB_URL = Config.get().getDbUrl();
        String DB_USER = Config.get().getDbUser();
        String DB_PASSWORD = Config.get().getDbPassword();
        storage = new SqlStorage(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        Writer writer = resp.getWriter();
        List<Resume> resumes = storage.getAllSorted();
        writer.write("<html>");
        writer.write("<head>");
        writer.write("<meta charset=\"UTF-8\">");
        writer.write("<title>Resumes</title>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("<table>");
        writer.write("<tr><th>UUID</th><th>FULL NAME</th><th>EMAIL</th></tr>");
        for (Resume r : resumes){
            writer.write("<tr><th>" + r.getUuid() + "</th><th>" + r.getFullName() + "</th><th>" + r.getContact(ContactType.EMAIL) + "</th></tr>");
        }
        writer.write("</table>");
        writer.write("</body>");
        writer.write("</html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
