package com.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@ManagedBean(name = "bookBean")
@SessionScoped
public class BookBean {

	private SessionFactory factory ;
	private ArrayList<Book> bookList;
	private Session session;

	@PostConstruct
	private void init()
	{

		System.out.println("Init called");
	

		try {
			StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();  
			Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
			factory = meta.getSessionFactoryBuilder().build();  
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			
			session.createSQLQuery("delete from authors where true ").executeUpdate();
			session.createSQLQuery("delete from books where true").executeUpdate();

			InputStream is = BookBean.class.getResourceAsStream("/bookstore.xml");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (is);
			doc.getDocumentElement().normalize();

			NodeList listOfBooks = doc.getElementsByTagName("book");

			for(int s=0; s<listOfBooks.getLength(); s++){
				Node firstBookNode = listOfBooks.item(s);
				if(firstBookNode.getNodeType() == Node.ELEMENT_NODE){

					Element firstBookElement = (Element)firstBookNode;

					NodeList titleList = firstBookElement.getElementsByTagName("title");
					Element titleElement =(Element)titleList.item(0);
					NodeList titleFNList = titleElement.getChildNodes();
					String title=((Node)titleFNList.item(0)).getNodeValue().trim();


					NodeList yearList = firstBookElement.getElementsByTagName("year");
					Element yearElement =(Element)yearList.item(0);
					NodeList yearFNList = yearElement.getChildNodes();
					String year=((Node)yearFNList.item(0)).getNodeValue().trim();


					NodeList priceList = firstBookElement.getElementsByTagName("price");
					Element priceElement =(Element)priceList.item(0);
					NodeList priceFNList = priceElement.getChildNodes();
					String price=((Node)priceFNList.item(0)).getNodeValue().trim();


					NodeList authorList = firstBookElement.getElementsByTagName("authors");

					Element authorElement =(Element)authorList.item(0);
					NodeList authorLists = authorElement.getElementsByTagName("author");

					Book b=new Book(title,year,price);
					int b_id=(int)session.save(b); 
					
					
					for(int k=0;k<authorLists.getLength();k++)
					{
						Element aElement =(Element)authorLists.item(k);
						NodeList authorFNList = aElement.getChildNodes();

						String author=((Node)authorFNList.item(0)).getNodeValue().trim();
						Author a=new Author(b_id,author);
						session.save(a);
						
					}
					
					

				}
			}

			t.commit();
			session.close();
			reloadBookList();
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}

	// Used to update record
	public void edit(int id){
		Book b=bookList.get(id);
		b.setEditable(true);	
	}


	// Used to save record
	public void save(Book b){
		session=factory.openSession();
		Transaction t=session.beginTransaction();
		session.update(b);
		int b_id=b.getId(); 
		session.createSQLQuery("delete from authors where b_id="+b_id).executeUpdate();
		String authorList []=b.getAuthors().split(",");
		for(int k=0;k<authorList.length;k++)
		{
			String author=authorList[k];
			Author a=new Author(b_id,author);
			session.save(a);
		}
		
		t.commit();
		session.close();
		reloadBookList();
		
	}

	// Used to save record
	public void delete(int id){

		session=factory.openSession();
		Transaction t=session.beginTransaction();
		session.createSQLQuery("delete from authors where b_id="+bookList.get(id).getId()).executeUpdate();
		session.remove(bookList.get(id));
		t.commit();
		session.close();
		reloadBookList();

	}


	public ArrayList<Book> getBookList() {
		return bookList;
	}

	public void setBookList(ArrayList<Book> bookList) {
		this.bookList = bookList;
	}
	
	private void reloadBookList()
	{
		session=factory.openSession();
		bookList=(ArrayList<Book>)session.createCriteria(Book.class).list();
		for(Book b:bookList)
		{
			String hql = " FROM Author A WHERE A.b_id ="+b.getId();
			List<Author> authorLists = session.createQuery(hql).list();
			
			String authors="";
			for(int k=0;k<authorLists.size();k++)
			{
				
				String author=authorLists.get(k).getAuthor();
				
				if(k==authorLists.size()-1)
				{
					authors=authors+author;
				}else {
					authors=authors+author+",";
				}

			}
			
			b.setAuthors(authors);
			b.setEditable(false);
		}
		
		session.close();
	}

	public void readXml()
	{
		try {
			
			session = factory.openSession();
			Transaction t = session.beginTransaction();
			InputStream is = BookBean.class.getResourceAsStream("/bookstore.xml");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (is);
			doc.getDocumentElement().normalize();

			NodeList listOfBooks = doc.getElementsByTagName("book");

			for(int s=0; s<listOfBooks.getLength(); s++){
				Node firstBookNode = listOfBooks.item(s);
				if(firstBookNode.getNodeType() == Node.ELEMENT_NODE){

					Element firstBookElement = (Element)firstBookNode;

					NodeList titleList = firstBookElement.getElementsByTagName("title");
					Element titleElement =(Element)titleList.item(0);
					NodeList titleFNList = titleElement.getChildNodes();
					String title=((Node)titleFNList.item(0)).getNodeValue().trim();


					NodeList yearList = firstBookElement.getElementsByTagName("year");
					Element yearElement =(Element)yearList.item(0);
					NodeList yearFNList = yearElement.getChildNodes();
					String year=((Node)yearFNList.item(0)).getNodeValue().trim();


					NodeList priceList = firstBookElement.getElementsByTagName("price");
					Element priceElement =(Element)priceList.item(0);
					NodeList priceFNList = priceElement.getChildNodes();
					String price=((Node)priceFNList.item(0)).getNodeValue().trim();


					NodeList authorList = firstBookElement.getElementsByTagName("authors");

					Element authorElement =(Element)authorList.item(0);
					NodeList authorLists = authorElement.getElementsByTagName("author");

					Book b=new Book(title,year,price);
					int b_id=(int)session.save(b); 
					
					
					for(int k=0;k<authorLists.getLength();k++)
					{
						Element aElement =(Element)authorLists.item(k);
						NodeList authorFNList = aElement.getChildNodes();

						String author=((Node)authorFNList.item(0)).getNodeValue().trim();
						Author a=new Author(b_id,author);
						session.save(a);
						
					}
					
					

				}
			}

			t.commit();
			session.close();
			reloadBookList();
		}catch(Exception e)
		{
			System.out.println(e);
		}
	
	}

}
