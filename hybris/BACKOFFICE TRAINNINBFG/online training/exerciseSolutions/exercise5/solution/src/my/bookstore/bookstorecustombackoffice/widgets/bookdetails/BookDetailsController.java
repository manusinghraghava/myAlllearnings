/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package my.bookstore.bookstorecustombackoffice.widgets.bookdetails;


import de.hybris.platform.core.model.media.MediaModel;

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;

import my.bookstore.core.model.BookModel;


// TODO exercise 5.3: have a look here and familiarize yourself with this class' implementation
public class BookDetailsController extends DefaultWidgetController
{

	public static final String SOCKET_SELECTED_BOOK = "selectedBook";

	public static final String WIDGET_SETTING_CONFIG_CONTEXT = "configContext";

	protected static final String CSS_CLASS_LABEL_PROPERTY_NAME = "propertyName";
	protected static final String CSS_CLASS_LABEL_PROPERTY_VALUE = "propertyValue";
	protected static final String IMAGE_MIME = "image/png";
	// TODO exercise 5.4a: define two new instance attribute here that correspond to the div components
	// from the .ZUL file whose IDs are 'imgDiv' and 'infoDiv'
	private Div imgDiv;
	private Div infoDiv;

	private static final Logger LOG = LoggerFactory.getLogger(BookDetailsController.class);
	@WireVariable
	private ObjectPreviewService objectPreviewService;

	private BookModel selectedBook;

	@Override
	public void initialize(final Component comp)
	{
		LOG.info("Initializing the BookDetails widget's controller");
		super.initialize(comp);
	}

	// TODO exercise 5.5a: add the annotation that will bind this method to be invoked
	// when a BookModel object arrives at the selectedBook socket
	@SocketEvent(socketId = SOCKET_SELECTED_BOOK)
	public void handleSelectedBook(final BookModel book)
	{
		LOG.info("Socket event is caught with Book: " + (book != null ? book.getName() : "null book reference"));
		setSelectedBook(book);
		populateImgDiv();
		populateInfoDiv();
	}

	private void setSelectedBook(final BookModel book)
	{
		selectedBook = book;
	}

	public BookModel getSelectedBook()
	{
		return selectedBook;
	}


	protected void populateImgDiv()
	{
		LOG.info("Starting to (re)populate the imgDiv of the BookDetails widget's view");

		// View needs to be cleared for null input, but also for every refresh, THUS, FOR ALL CASES
		clearImgDiv();

		if (getSelectedBook() != null) //if book was null, the controller's job was to clear which is done either way
		{
			final Base baseConfig = loadConfiguration(BookModel._TYPECODE);
			populateImgDiv(getSelectedBook(), baseConfig);
		}
	}


	protected void populateInfoDiv()
	{
		LOG.info("Starting to (re)populate the infoDiv of the BookDetails widget's view");

		// View needs to be cleared for null input, but also for every refresh, THUS, FOR ALL CASES
		clearInfoDiv();

		if (getSelectedBook() != null) //if book was null, the controller's job was to clear which is done either way
		{
			final Base baseConfig = loadConfiguration(BookModel._TYPECODE);
			populateInfoDiv(getSelectedBook(), baseConfig);
		}
	}


	protected Base loadConfiguration(final String typeCode)
	{
		Base config = null;

		final DefaultConfigContext configContext = new DefaultConfigContext(
				getWidgetSettings().getString(WIDGET_SETTING_CONFIG_CONTEXT));
		configContext.setType(typeCode);

		try
		{
			config = getWidgetInstanceManager().loadConfiguration(configContext, Base.class);

			if (config == null)
			{
				LOG.warn("Loaded UI configuration is null. Ignoring.");
			}
		}
		catch (final CockpitConfigurationNotFoundException ccnfe)
		{
			LOG.info("Could not find UI configuration for given context (" + configContext + ").", ccnfe);
		}
		catch (final CockpitConfigurationException cce)
		{
			LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
		}
		return config;
	}


	private void clearImgDiv()
	{
		/*
		 * TODO exercise 5.4b: Now that the attributes has been defined, uncomment the next line to allow this method to do its
		 * job the line of code that manipulate imgDiv
		 */
		this.imgDiv.getChildren().clear();
	}


	private void clearInfoDiv()
	{
		/*
		 * TODO exercise 5.4c: Now that the attributes has been defined, uncomment the next line to allow this method to do its
		 * job the line of code that manipulate imgDiv
		 */
		this.infoDiv.getChildren().clear();
	}


	private void populateImgDiv(final BookModel book, final Base config)
	{
		final MediaModel picture = (book != null) ? book.getPicture() : null;

		// The following SHOULD work, but I believe it retrieves the image from an index, so it ignores
		// immediate changes made to the Book (Product) 'picture' attribute.
		// Leaving this commented-out code as a real-world example.
		//		final ObjectPreview thumbnail = (picture != null && config != null) ? objectPreviewService.getPreview(book, config)
		//				: objectPreviewService.getPreview(IMAGE_MIME);
		//		final String url = (thumbnail != null) ? thumbnail.getUrl() : "";

		final String url = (picture != null) ? picture.getURL() : objectPreviewService.getPreview(IMAGE_MIME).getUrl();

		/*
		 * TODO exercise 5.4d: Now that the attributes has been defined, uncomment the next line to allow this method to do its
		 * job the line of code that manipulate imgDiv
		 */
		this.imgDiv.setHeight((picture != null) ? "96px" : "100%");
		if (url != null)
		{
			/*
			 * TODO exercise 5.4e: Now that the attributes has been defined, uncomment the next line to allow this method to do its
			 * job the line of code that manipulate imgDiv
			 */
			this.imgDiv.appendChild(createImageElement(url));
		}
		LOG.info("Image div created! URL: " + url);
	}

	private Image createImageElement(final String imageUrl)
	{
		final Image img;
		try
		{
			img = new Image(imageUrl);
		}
		catch (final IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}

		img.setHeight("100%");

		return img;
	}

	private void populateInfoDiv(final BookModel book, final Base config)
	{
		final Vlayout vlayout = new Vlayout();

		/*
		 * TODO exercise 5.4f: Now that the attributes has been defined, uncomment the next line to allow this method to do its
		 * job the line of code that manipulate imgDiv
		 */
		vlayout.setParent(this.infoDiv);

		addProperty(vlayout, "Title", book.getName());

		final StringJoiner joiner = new StringJoiner(", ");
		book.getAuthors().forEach(a -> joiner.add(a.getName()));
		final String authors = joiner.toString();

		addProperty(vlayout, "Authors", authors);
		addProperty(vlayout, "ISBN10", book.getISBN10());
		addProperty(vlayout, "ISBN13", book.getISBN13());
		addProperty(vlayout, "Description", book.getDescription());

		LOG.info("Info div created! Title: " + book.getName());
	}

	private void addProperty(final Vlayout parent, final String propertyTitle, final String propertyContent)
	{
		final Hlayout hlayout = new Hlayout();
		final Label title = new Label(propertyTitle + ":");
		final Vlayout contentVlayout = new Vlayout();
		final Label content = new Label(propertyContent != null ? propertyContent : "-");
		content.setMultiline(true);
		content.setHflex("1");
		contentVlayout.appendChild(content);
		contentVlayout.setHflex("1");
		hlayout.appendChild(title);
		hlayout.appendChild(contentVlayout);
		UITools.modifySClass(title, CSS_CLASS_LABEL_PROPERTY_NAME, true);
		UITools.modifySClass(contentVlayout, CSS_CLASS_LABEL_PROPERTY_VALUE, true);
		parent.appendChild(hlayout);
	}

}
