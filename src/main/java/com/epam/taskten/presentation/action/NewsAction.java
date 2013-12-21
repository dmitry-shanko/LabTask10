package com.epam.taskten.presentation.action;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.MappingDispatchAction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.taskten.database.exception.DaoException;
import com.epam.taskten.model.News;
import com.epam.taskten.presentation.facade.NewsFacade;
import com.epam.taskten.presentation.form.NewsForm;
import com.epam.taskten.util.DateFormatter;
import com.epam.taskten.util.GlobalConstants;

import com.google.gson.Gson;

public class NewsAction extends MappingDispatchAction
{
	private static final Logger log = LoggerFactory.getLogger(NewsAction.class);
	private static final String resources = "jsp.MessageResources";
	private static final String datePattern = "date.pattern";

	private NewsFacade newsFacade;
	private Gson gson;

	private static final String MAIN_PAGE = "mainpage";
	private static final String NEWS_LIST_PAGE = "newsList";
	private static final String VIEW_NEWS_PAGE = "newsView";
	private static final String VIEW_NEWS_PAGE_ACTION = "newsViewAction";
	private static final String EDIT_NEWS_PAGE = "newsEdit";
	private static final String ERROR_PAGE = "error";
	private static final String PREVIOUS_PAGE = "previousPage";
	private static final String REFERER = "Referer";

	public NewsAction()
	{
		log.info("com.epam.taskten.presentation.action.NewsAction has been created");
	}

	public void setNewsFacade(NewsFacade newsFacade)
	{
		this.newsFacade = newsFacade;
	}
	/**
	 * @return the gson
	 */
	public Gson getGson() {
		return gson;
	}

	/**
	 * @param gson the gson to set
	 */
	public void setGson(Gson gson) {
		this.gson = gson;
	}

	/**
	 * Forwards to the News list page with list of news got from newsDao.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward newsList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward newsList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newslist");
		String target = NEWS_LIST_PAGE;
		request.getSession().setAttribute(PREVIOUS_PAGE, MAIN_PAGE);
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /view set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
		}
		log.info("/newslist action finished.");
		log.debug("actionMapping.findForward(target) in /newslist action: target={}", target);
		return actionMapping.findForward(target);
	}

	/**
	 * Appends JSON representation of List<News> collected from database to HttpServletResponse response.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void getJsonList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public void getJsonList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/list");
		NewsForm newsForm = (NewsForm) actionForm;
		if (null != newsForm)
		{
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /list set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
			List<News> newsList = null;
			try 
			{
				newsList = newsFacade.getNewsList();
			} 
			catch (DaoException e) 
			{
				log.error("Can't complete /list action. DaoException catched.", e);
			}
			if (newsList != null) 
			{				
				try 
				{
					response.setHeader("Cache-Control","no-cache"); 
					response.setHeader("Pragma","no-cache");
					response.getWriter().append(gson.toJson(newsList));
					response.getWriter().close();
				}
				catch (IOException e) 
				{
					log.error("Can't complete JSON convertation.", e);
				}
				log.debug("Args in /list action (got from database): news={}", newsList);
			}
		}
		else
		{
			log.warn("newsForm in /list is null.");
		}
		log.info("/list action finished.");
	}
	/**
	 * Forwards to the View page with news in newsForm to be viewed
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newsView(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction newsView(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsview");
		String target = VIEW_NEWS_PAGE;
		request.getSession().setAttribute(PREVIOUS_PAGE, VIEW_NEWS_PAGE_ACTION);
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /view set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
		}
		int newsId = 0;
		try
		{
			newsId = Integer.parseInt(request.getParameter("newsId"));
		}
		catch (NumberFormatException e)
		{
			log.warn("NumberFormatException catched in /newsview action", e);
		}
		request.setAttribute("newsId", newsId);
		log.info("/newsview action finished.");
		log.debug("actionMapping.findForward(target) in /newsview action: target={}", target);
		return actionMapping.findForward(target);
	}

	public void getJsonNewsToView(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction getJsonNewsToView(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/view");
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /view set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
			News news = null;
			try 
			{
				int newsId = 0;
				try
				{
					newsId = Integer.parseInt(request.getParameter("newsId"));
				}
				catch(NumberFormatException e)
				{
					log.info("incorrect newsId in /view action");
				}
				log.info("Args in /view action: newsId={}", newsId);
				news = newsFacade.fetchById(newsId);
			} 
			catch (DaoException e) 
			{
				log.error("Can't complete /view action. DaoException catched.", e);
			}
			log.info("Args in /view action (got from database): news={}", news);
			try 
			{
				if (null == news)
				{
					news = new News();
					news.setId(0);
				}
				response.setHeader("Cache-Control","no-cache"); 
				response.setHeader("Pragma","no-cache");
				response.getWriter().append(gson.toJson(news));
				response.getWriter().close();
			}
			catch (IOException e) 
			{
				log.error("Can't complete JSON convertation.", e);
			}			
		}	
		else
		{
			log.warn("newsForm in /view is null.");
		}
		log.info("/view action finished.");
	}
	/**
	 * Forwards to the Edit page with news in newsForm to be edited.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newsEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward newsEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsedit");
		String target = EDIT_NEWS_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{		
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /newslist set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
		}
		int newsId = 0;
		try
		{
			newsId = Integer.parseInt(request.getParameter("newsId"));
		}
		catch (NumberFormatException e)
		{
			log.warn("NumberFormatException catched in /newsview action", e);
		}
		request.setAttribute("newsId", newsId);			
		log.info("/newsedit action finished.");
		log.debug("actionMapping.findForward(target) in /newsedit action: target={}", target);
		return actionMapping.findForward(target);
	}	

	public void getJsonNewsToEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward getJsonNewsToEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/edit");
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /view set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
		}
		News news = null;
		int newsId = 0;
		try
		{
			newsId = Integer.parseInt(request.getParameter("newsId"));
		}
		catch(NumberFormatException e)
		{
			log.info("incorrect newsId in /edit action");
		}
		log.info("Args in /edit action: newsId={}", newsId);
		if (newsId > 0)
		{
			try 
			{
				news = newsFacade.fetchById(newsId);
			} 
			catch (DaoException e) 
			{
				log.error("Can't complete /edit action. DaoException catched.", e);
			}
		}
		log.info("Args in /edit action (got from database): news={}", news);
		try 
		{
			if (null == news)
			{
				news = new News();
				news.setId(0);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new java.util.Date(System.currentTimeMillis()));
				news.setNewsDate(calendar);
			}
			response.setHeader("Cache-Control","no-cache"); 
			response.setHeader("Pragma","no-cache");
			response.getWriter().append(gson.toJson(news));
			response.getWriter().close();
		}
		catch (IOException e) 
		{
			log.error("Can't complete JSON convertation.", e);
		}	
		log.info("/edit action finished.");
	}
	/**
	 * Forwards to the Edit page without any news to add.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newsAdd(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward newsAdd(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsadd");
		String target = EDIT_NEWS_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{				
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /newslist set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
			News news = new News();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new java.util.Date(System.currentTimeMillis()));
			news.setNewsDate(calendar);
			log.debug("Args in /newsadd action: emptyNews={}", news);
			newsForm.setNews(news);
		}
		else
		{
			log.warn("newsForm in /newsadd is null.");
		}
		log.info("/newsadd action finished.");
		log.debug("actionMapping.findForward(target) in /newsadd action: target={}", target);
		return actionMapping.findForward(target);
	}	
	/**
	 * Saves changed news. Creates new if id of news from newsForm.getNews() is less then 1 and updates existing if id is 1 or more.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	@Deprecated
	public ActionForward newsSave(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward newsSave(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newssave");
		String target = ERROR_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			News news = newsForm.getNews();
			log.info("Args in /newssave action: news={}", news);
			if ((null != news) && (news.getBrief() != null) && (news.getContent() != null) && (news.getNewsDate() != null) && (news.getTitle() != null))
			{
				try 
				{					
					if (news.getId() < 1)
					{
						if (newsFacade.save(news) != null)
						{
							target = VIEW_NEWS_PAGE_ACTION;
							log.info("News have been successfully saved: savedNews={}", news);
						}
						else
						{							
							log.warn("News were not saved: notSavedNews={}", news);
						}
					}
					else
					{
						if (newsFacade.updateNews(news) != null)
						{
							target = VIEW_NEWS_PAGE_ACTION;
							log.info("News have been successfully updated: updatedNews={}", news);
						}
						else
						{
							log.warn("News were not updated: notUpdatedNews={}", news);
						}
					}
				}
				catch (DaoException e) 
				{
					log.error("Can't complete /newssave action. DaoException catched.", e);
				}
			}
		}	
		else
		{
			log.warn("newsForm in /newssave is null.");
		}
		log.info("/newssave action finished.");
		log.debug("ActionRedirect(actionMapping.findForward(target)) in /newssave action: target={}", target);
		return new ActionRedirect(actionMapping.findForward(target));
	}

	public void ajaxNewsSave(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward ajaxNewsSave(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/save");
		String newsTitle = request.getParameter("newsTitle");
		String newsBrief = request.getParameter("newsBrief");
		String newsContent = request.getParameter("newsContent");
		String dateString = request.getParameter("dateString");
		int newsId = 0;
		try
		{
			newsId = Integer.parseInt(request.getParameter("newsId"));
		}
		catch(NumberFormatException e)
		{
			log.info("incorrect newsId in /view action");
		}
		log.debug("Args in /save action: newsId={}, newsTitle={}, dateString={}, newsBrief={}, newsContent={}", new Object[]{newsId, newsTitle, dateString, newsBrief, newsContent});
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			News news = new News();
			news.setBrief(newsBrief);
			news.setContent(newsContent);
			news.setTitle(newsTitle);
			news.setId(newsId);
			if (dateString != null && !(dateString.isEmpty())) 
			{
				ResourceBundle bundle = ResourceBundle.getBundle(resources, newsForm.getLocale());
				String pattern = bundle.getString(datePattern);
				DateFormat dateFormat = new SimpleDateFormat(pattern);
				java.util.Date utilDate = null;
				try 
				{
					log.debug("Attempt to parse String date into java.util.Date using pattern: date={} into format={}.", dateString, pattern);
					utilDate = dateFormat.parse(dateString);
				} 
				catch (ParseException e) 
				{
					log.error("Can't parse String ".concat(dateString).concat(" into java.util.Date ").concat(pattern).concat(". ParseException:"), e);
				}
				Date sqlDate = DateFormatter.utilDateToSqlDate(utilDate);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new java.util.Date(sqlDate.getTime()));
				news.setNewsDate(calendar);
				log.debug("date set to news: news.newsDate={}", news.getNewsDate());
			}
			try 
			{
				if ((news.getId() != null) && (news.getId() > 0))
				{
					if (newsFacade.updateNews(news) != null)
					{
						log.info("News have been successfully updated: updatedNews={}", news);
					}
					else
					{
						log.warn("News were not updated: notUpdatedNews={}", news);
					}
				}
				else
				{
					if (newsFacade.save(news) != null)
					{
						log.info("News have been successfully saved: savedNews={}", news);
					}
					else
					{							
						log.warn("News were not saved: notSavedNews={}", news);
					}
				}
			}
			catch (DaoException e) 
			{
				log.error("Can't complete /save action. DaoException catched.", e);
			}
			try 
			{
				response.setHeader("Cache-Control","no-cache"); 
				response.setHeader("Pragma","no-cache");
				response.getWriter().append(gson.toJson(news));
				response.getWriter().close();
			}
			catch (IOException e) 
			{
				log.error("Can't complete JSON convertation.", e);
			}				
		}
		log.info("/save action finished.");
	}
	/**
	 * This mapped action deletes list of news with ID taken from newsForm.getSelectedItems().
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Deprecated
	public ActionForward newsDelete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward newsDelete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsdelete");
		String target = NEWS_LIST_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			Integer[] ids = newsForm.getSelectedItems();	
			log.info("Args in /newsdelete action: ids={}", ids);
			if ((null != ids) && (ids.length > 0))
			{
				try 
				{
					newsFacade.remove(ids);
					target = MAIN_PAGE;
					log.debug("/newsdelete action completed. Id of deleted News are in NewsDao Logger");
				} 
				catch (DaoException e) 
				{
					log.error("Can't complete /newsdelete action. DaoException catched.", e);
				}
			}
		}
		else
		{
			log.warn("newsForm in /newsdelete is null.");
		}
		log.info("/newsdelete action finished.");
		log.debug("ActionRedirect(actionMapping.findForward(target)) in /newsdelete action: target={}", target);
		return new ActionRedirect(actionMapping.findForward(target));
	}

	public void ajaxDelete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward ajaxDelete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/delete");
		String[] array = request.getParameterValues("newsId");
		for (String s : array)
		{
			try
			{
				int n = Integer.parseInt(s);
				try 
				{
					log.info("Attempt to delete news.id={}", n);
					newsFacade.remove(n);
				} 
				catch (DaoException e) 
				{
					log.error("Can't delete news with news.id={}", e);
				}
			}
			catch (NumberFormatException e)
			{
				log.info("incorrect newsId in /delete action");
			}
		}
		log.info("/delete action finished.");
	}

	public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)");
		log.info("NewsActionServlet: action=/cancel");
		String target = (String) request.getSession().getAttribute(PREVIOUS_PAGE);
		request.setAttribute("newsId", request.getParameter("newsId"));
		log.debug("actionMapping.findForward(target) in /cancel action: target={}", target);
		return actionMapping.findForward(target);
	}
	/**
	 * In the case of error forwards to the Error page.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward error(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward error(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)");
		log.info("NewsActionServlet: action=/error");
		log.debug("actionMapping.findForward(ERROR_PAGE) in /error action: target={}", ERROR_PAGE);
		return actionMapping.findForward(ERROR_PAGE);
	}
	/**
	 * Mapped ActionForward for changing language. Forwards to the request.getHeader("Referer") link.<p>
	 * Parameter of new locale is taking from request.
	 * @param actionMapping
	 * @param actionForm 
	 * @param request
	 * @param response
	 * @return new ActionForward(request.getHeader("Referer"), true)
	 * @throws Exception
	 */
	public ActionForward lang(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.taskten.presentation.action.NewsAction public ActionForward lang(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)");
		log.info("NewsActionServlet: action=/lang");
		String lang = request.getParameter(GlobalConstants.LANG_PARAM_REQUEST.getContent()); 	
		log.debug("request.getParameter(GlobalConstants.LANG_PARAM_REQUEST.getContent()): lang={}", lang);
		if (null != lang)
		{
			String country = GlobalConstants.COUNTRY_EN.getContent();
			lang = lang.trim().toLowerCase();
			switch (lang)
			{
			case "ru":
				country = GlobalConstants.COUNTRY_RU.getContent();
				break;
			case "en":
				country = GlobalConstants.COUNTRY_EN.getContent();
				break;
			}
			Locale locale = new Locale(lang, country);
			setLocale(request, locale);
			NewsForm newsForm = (NewsForm) actionForm;
			newsForm.setLocale(locale);	
			log.info("Locale after /lang action: locale={}", locale);
		}
		else
		{
			log.warn("Attempt to change locale to null");
		}
		String target = request.getHeader(REFERER);
		log.info("/lang action finished.");
		log.debug("ActionForward(target, true) in /lang action: target={}", target);
		return new ActionForward(target, true);
	}
}
