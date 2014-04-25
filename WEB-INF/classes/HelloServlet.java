import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;


public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String hostname = getHostname();
            String dbConnectString = getDBConnectionString();

            render(resp.getWriter(), hostname, dbConnectString);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void render(PrintWriter out, String hostname, String dbConnectionString) {
        out.println("<html><body>");

        out.println("<h1>Tomcat Demo App</h1>");
        out.println("<b>Machine:</b> " + hostname);
        out.println("</br>");
        out.println("<b>ConnectionString:</b> " + dbConnectionString);

        out.println("</body></html>");
        out.flush();
        out.close();
    }

    private static String getDBConnectionString() throws Exception {
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgreSqlDataSource");

        return ds.getConnection().getMetaData().getURL();
    }

    private static String getHostname() throws Exception {
        try {
            Process proc = Runtime.getRuntime().exec("hostname");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            return reader.readLine();
        } catch (Exception e) {
            return InetAddress.getLocalHost().getHostName();
        }
    }
}