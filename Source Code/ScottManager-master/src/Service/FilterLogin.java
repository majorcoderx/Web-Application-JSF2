package Service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Controller.Login;

public class FilterLogin implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		Login session = (Login) req.getSession().getAttribute("login");
		String url = req.getRequestURI();
		/*
		 * A. if request every one, there is't session, redirect the request to index.xhtml
		 * B. if request is logout, there is session, remove session
		 * 
		 */
		if(session == null || !StaticValue.isLogged){
			if(url.indexOf("index.xhtml")>=0){
				chain.doFilter(request, response);
			}
			else{
				resp.sendRedirect(req.getServletContext().getContextPath() + "/Views/Share/index.xhtml?faces-redirect=true");
			}
		}
		else{
			if(url.indexOf("index.xhtml")>=0){
				StaticValue.isLogged = false;
				req.getSession().removeAttribute("login");
				resp.sendRedirect(req.getServletContext().getContextPath()+"/Views/Share/index.xhtml?faces-redirect=true");
			}
			else{
				chain.doFilter(request, response);
			}
		}
		
//		if(req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)){
//			session.setLogged(false);
//			resp.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
//			resp.setHeader("Prama", "no-cache");
//			resp.setDateHeader("Expires", 0);
//		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
