package be.vdab.servlets.artikels;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vdab.entities.Artikel;
import be.vdab.entities.FoodArtikel;
import be.vdab.entities.NonFoodArtikel;
import be.vdab.services.ArtikelService;

/**
 * Servlet implementation class ToevoegenServlet
 */
@WebServlet("/artikels/toevoegen.htm")
public class ToevoegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/artikels/toevoegen.jsp";
	private static final String REDIRECT_URL = "%s/artikels/zoekenopnummer.htm?id=%d";

	private static transient ArtikelService artikelService = new ArtikelService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> fouten = new HashMap<>();
		String naam = request.getParameter("naam");
		if (!Artikel.isNaamValid(naam)) {
			fouten.put("naam", "verplicht");
		}

		BigDecimal aankoopprijs = null;
		try {
			aankoopprijs = new BigDecimal(request.getParameter("aankoopprijs"));
			if (!Artikel.isAankoopprijsValid(aankoopprijs)) {
				fouten.put("aankoopprijs", "tik een positief getal of 0");
			}
		} catch (NumberFormatException ex) {
			fouten.put("aankoopprijs", "tik een positief getal of 0");
		}

		BigDecimal verkoopprijs = null;
		try {
			verkoopprijs = new BigDecimal(request.getParameter("verkoopprijs"));
			if (!Artikel.isVerkoopprijsValid(verkoopprijs)) {
				fouten.put("verkoopprijs", "tik een positief getal of 0");
			} else {
				if (!Artikel.isAankoopprijsVerkoopprijsValid(aankoopprijs,
						verkoopprijs)) {
					fouten.put("verkoopprijs",
							"verkoopprijs kan niet kleiner zijn dan de aankoopprijs");
				}
			}
		} catch (NumberFormatException ex) {
			fouten.put("verkoopprijs", "tik een positief getal of 0");
		}
		
		int houdbaarheid = 0;
		int garantie = 0;
		String soort = request.getParameter("soort");
		if (soort == null) {
			fouten.put("soort", "maak een keuze");
		} else {
			switch (soort) {
			case "F":
				try {
					houdbaarheid = Integer.parseInt(request
							.getParameter("houdbaarheid"));
					if (!FoodArtikel.isHoudbaarheidValid(houdbaarheid)) {
						fouten.put("houdbaarheid", "tik een positief getal");
					}
				} catch (NumberFormatException ex) {
					fouten.put("houdbaarheid", "tik een positief getal");
				}
				break;
			case "NF":
				try {
					garantie = Integer.parseInt(request
							.getParameter("garantie"));
					if (!NonFoodArtikel.isGarantieValid(garantie)) {
						fouten.put("garantie", "tik een positief getal of 0");
					}
				} catch (NumberFormatException ex) {
					fouten.put("garantie", "tik een positief getal of 0");
				}
				break;
			default:
				fouten.put("soort", "maak een keuze");
			}
		}

		if (fouten.isEmpty()) {
			Artikel artikel;
			if ("F".equals(soort)) {
				artikel = new FoodArtikel(naam, aankoopprijs,
						verkoopprijs, houdbaarheid);
			} else {
				artikel = new NonFoodArtikel(naam, aankoopprijs,
						verkoopprijs, 10);
			}

			artikelService.create(artikel);
			response.sendRedirect(response.encodeRedirectURL(String.format(
					REDIRECT_URL, request.getContextPath(), artikel.getId())));
		} else {
			request.setAttribute("fouten", fouten);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	}

}
