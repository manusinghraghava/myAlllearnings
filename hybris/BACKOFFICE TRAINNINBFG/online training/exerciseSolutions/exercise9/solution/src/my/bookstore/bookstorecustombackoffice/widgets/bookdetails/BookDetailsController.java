/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package my.bookstore.bookstorecustombackoffice.widgets.bookdetails;


import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;

import my.bookstore.bookstorecustombackoffice.config.details.jaxb.Details;
import my.bookstore.bookstorecustombackoffice.config.details.jaxb.Property;
import my.bookstore.core.model.BookModel;



public class BookDetailsController extends DefaultWidgetController
{
	public static final String WIDGET_MODEL_ATTRIBUTE_CURRENT_BOOK = "currentBook";
	public static final String SOCKET_SELECTED_BOOK = "selectedBook";
	public static final String SOCKET_ALLOW_PREVIEW = "allowPreview";

	public static final String WIDGET_SETTING_CONFIG_CONTEXT = "configContext";
	public static final String WIDGET_SETTING_PROPERTIES_CONFIG_CONTEXT = "propertiesConfigContext";

	protected static final String CSS_CLASS_LABEL_PROPERTY_NAME = "propertyName";
	protected static final String CSS_CLASS_LABEL_PROPERTY_VALUE = "propertyValue";
	protected static final String IMAGE_MIME = "image/png";
	protected static final String IMAGE_POPUP_POSITION = "start_before";

	// 2-way-bound ZK ZUL components
	protected Div imgDiv;
	protected Div infoDiv;
	protected Popup bookImagePopup;

	@WireVariable
	protected PropertyValueService propertyValueService;

	private static final Logger LOG = LoggerFactory.getLogger(BookDetailsController.class);
	@WireVariable
	protected ObjectPreviewService objectPreviewService;
	@WireVariable
	protected CockpitLocaleService cockpitLocaleService;

	private BookModel selectedBook;

	@Override
	public void initialize(final Component comp)
	{
		LOG.info("Initializing the BookDetails widget's controller");
		super.initialize(comp);
	}

	@SocketEvent(socketId = SOCKET_SELECTED_BOOK)
	public void handleSelectedBook(final BookModel book)
	{
		LOG.info("Socket event is caught with Book: " + (book != null ? book.getName() : "null book"));
		setSelectedBook(book);

		getModel().put(WIDGET_MODEL_ATTRIBUTE_CURRENT_BOOK, book);

		clearImgDiv();
		clearInfoDiv();

		if (book != null)
		{
			populateImgDiv();
			populateInfoDiv();
		}
	}

	@SocketEvent(socketId = SOCKET_ALLOW_PREVIEW)
	public void handleAllowPreview(final Boolean allowPreview)
	{
		LOG.info("Socket event is caught -- allowPreview: " + allowPreview);
		clearImagePopup(); // popup needs to be cleared for null or FALSE, but also just prior to a refresh, THUS, FOR ALL CASES
		if (Boolean.TRUE.equals(allowPreview))
		{
			populateImagePopup();
		}
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
			final Base baseConfig = loadConfiguration(BookModel._TYPECODE, WIDGET_SETTING_CONFIG_CONTEXT, Base.class);
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
			final Details propertiesConfig = loadConfiguration(BookModel._TYPECODE, WIDGET_SETTING_PROPERTIES_CONFIG_CONTEXT,
					Details.class);
			if (propertiesConfig != null)
			{
				propertiesConfig.getProperty().forEach(p -> LOG.info("Property to show: " + p.getName()));
			}
			populateInfoDiv(getSelectedBook(), propertiesConfig);
		}
	}


	protected <TYPE> TYPE loadConfiguration(final String typeCode, final String configComponent,
			final Class<TYPE> configurationType)
	{
		TYPE config = null;

		final DefaultConfigContext configContext = new DefaultConfigContext(getWidgetSettings().getString(configComponent));
		configContext.setType(typeCode);

		try
		{
			config = getWidgetInstanceManager().loadConfiguration(configContext, configurationType);

			if (config != null)
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


	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT)
	public void handleObjectsUpdatedEvent(final CockpitEvent event)
	{
		LOG.info("GlobalCockpitEventHandler.handleObjectsUpdatedEvent() called");

		final BookModel updatedBook = getEventObjectCurrentlyInView(event);
		if (updatedBook != null)
		{
			// Recall that this widget's 'allowPreview' socket was merely an excuse to teach you how to use an Adapter widget
			// and that there are less-problematic ways to solve the issue of conditional pop-up images.
			// The 'Adapter' approach we used leaves us little choice but to trigger a refresh of our view by simulating/replicating
			// the usual sequence of socket events that trigger our widget to generate view components

			getModel().put(WIDGET_MODEL_ATTRIBUTE_CURRENT_BOOK, updatedBook);

			handleSelectedBook(updatedBook);
			handleAllowPreview(Boolean.valueOf(updatedBook.getPicture() != null));
		}
	}


	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT)
	public void handleObjectsDeletedEvent(final CockpitEvent event)
	{
		LOG.info("GlobalCockpitEventHandler handleObjectsDeletedEvent() called");
		final BookModel deletedBook = getEventObjectCurrentlyInView(event);
		if (deletedBook != null)
		{
			// Recall that this widget's 'allowPreview' socket was merely an excuse to teach you how to use an Adapter widget
			// and that there are less-problematic ways to solve the issue of conditional pop-up images.
			// The 'Adapter' approach we used leaves us little choice but to trigger a refresh of our view by simulating/replicating
			// the usual sequence of socket events that trigger our widget to generate view components

			getModel().put(WIDGET_MODEL_ATTRIBUTE_CURRENT_BOOK, null);

			handleSelectedBook(null);
			handleAllowPreview(Boolean.FALSE);
		}
	}

	/**
	 * Extracts the latest version of the "current object" being displayed in our Book Details widget view, if it was
	 * involved in the CRUD GlobalCockpitEvent, otherwise returns null. Checks a collection of objects attached to event,
	 * possibly from a bulk edit (update) or bulk delete.
	 *
	 * @param event
	 *           the CockpitEvent object that was given to the event handler -- used to retrieve data Object(s) that caused
	 *           the CRUD event
	 * @return the latest version of the (BookModel) object being displayed, if it was part of the event data, or null
	 */
	private BookModel getEventObjectCurrentlyInView(final CockpitEvent event)
	{
		final Collection eventDataObjects = event.getDataAsCollection();
		for (final Object iEventDataObject : eventDataObjects)
		{
			logProcessingOfCrudEventDataObject(iEventDataObject);

			// All we really need to know is which (if any) object involved in the CRUD event is currently being displayed
			// in our widget's view, and what is its new state
			if (iEventDataObject.equals(this.selectedBook))
			{
				LOG.info("****** This event data object IS CURRENTLY BEING DISPLAYED by Book Details!!!");
				return (BookModel) iEventDataObject;
			}
		}
		return null;
	}


	private void logProcessingOfCrudEventDataObject(final Object eventDataObject)
	{
		String logMessage = "-->BookDetailsController.getEventObjectCurrentlyInView() checking object " + eventDataObject;
		if (eventDataObject instanceof BookModel)
		{
			final BookModel iEventBookModelObject = (BookModel) eventDataObject;
			logMessage += " " + iEventBookModelObject.getName() + ":" + iEventBookModelObject.getCatalogVersion().getVersion();
		}
		LOG.info(logMessage);
	}


	private void clearImgDiv()
	{
		this.imgDiv.getChildren().clear();
	}


	private void clearInfoDiv()
	{
		this.infoDiv.getChildren().clear();
	}


	private void clearImagePopup()
	{
		this.bookImagePopup.getChildren().clear();
		if (this.imgDiv != null)
		{
			this.imgDiv.setPopup("");
			this.imgDiv.setTooltiptext("");
		}
	}

	void populateImgDiv(final BookModel book, final Base config)
	{
		final MediaModel picture = (book != null) ? book.getPicture() : null;

		// The following SHOULD work, but I believe it retrieves the image from an index, so it ignores
		// immediate changes made to the Book (Product) 'picture' attribute.
		// Leaving this commented-out code as a real-world example.
		//		final ObjectPreview thumbnail = (picture != null && config != null) ? objectPreviewService.getPreview(book, config)
		//				: objectPreviewService.getPreview(IMAGE_MIME);
		//		final String url = (thumbnail != null) ? thumbnail.getUrl() : "";

		final String url = (picture != null) ? picture.getURL() : objectPreviewService.getPreview(IMAGE_MIME).getUrl();
		this.imgDiv.setHeight((picture != null) ? "96px" : "100%");

		if (url != null)
		{
			this.imgDiv.appendChild(createImageElement(url));
		}
		LOG.info("Image div populated! URL: " + url);
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


	/**
	 * Populates the bound ZK popup component with a larger product photo. Assigns the popup component to the existing
	 * thumbnail image. When the thumbnail is clicked, its larger-photo popup will appear.
	 */
	private void populateImagePopup()
	{
		if (getSelectedBook() == null)
		{
			LOG.error("Cannot create book popup for null book.");
		}
		if (getSelectedBook().getPicture() == null)
		{
			LOG.error("Cannot create book popup for null picture of book: " + getSelectedBook().getName() + ":"
					+ getSelectedBook().getCatalogVersion());
		}

		this.imgDiv.setPopup(this.bookImagePopup.getId() + ", position=" + IMAGE_POPUP_POSITION);
		this.imgDiv.setTooltiptext("Click to zoom!");

		final Div div = new Div();
		div.setHflex("min");
		final Optional<String> imageUrl = Optional.ofNullable("~" + getSelectedBook().getPicture().getURL());
		imageUrl.ifPresent(url -> div.appendChild(createImageElement(url)));
		this.bookImagePopup.appendChild(div);
		LOG.info("Image popup created! URL: " + imageUrl.orElse("no URL is available"));
	}


	void populateInfoDiv(final BookModel book, final Details config)
	{
		final Vlayout vlayout = new Vlayout();
		vlayout.setParent(this.infoDiv);

		// if config is not null and the property list is not empty
		if (config != null && config.getProperty() != null && config.getProperty().size() > 0)
		{
			// then for each property, get its "name" and then retrieve the property with the same name from the book
			for (final Property property : config.getProperty())
			{
				final String value = getPropertyValue(property.getName(), book);
				// if such a property is not null
				if (value != null)
				{
					// then add it to the view
					addProperty(vlayout, property.getLabel(), value);
				}
			}
		}

		LOG.info("Info div populated! Title: " + book.getName());
	}


	/**
	 * Uses propertyValueService and cockpitLocalService to retrieve the localized value of a attribute in a
	 * {@link BookModel} object.
	 *
	 * @param name
	 *           the name of the attribute to be fetched
	 * @param book
	 *           the object that the attribute's value is read from
	 * @return The localized value of the the attribute, or an empty string if no value is available
	 */
	protected String getPropertyValue(final String name, final BookModel book)
	{
		String propertyValue = "";
		final Object propertyValueObj = propertyValueService.readValue(book, name);

		if (propertyValueObj != null)
		{
			if (propertyValueObj instanceof Map)
			{
				final Object val = ((Map) propertyValueObj).get(cockpitLocaleService.getCurrentLocale());
				if (val != null)
				{
					propertyValue = val.toString();
				}
			}
			else
			{
				if (propertyValueObj instanceof List)
				{
					final StringJoiner joiner = new StringJoiner(", ");
					for (final Object listElement : (List) propertyValueObj)
					{
						if (listElement instanceof AbstractItemModel)
						{
							joiner.add(((AbstractItemModel) listElement).getProperty("name", cockpitLocaleService.getCurrentLocale()));
						}
						else
						{
							joiner.add(listElement.toString());
						}
					}
					propertyValue = joiner.toString();
				}
				else
				{
					propertyValue = propertyValueObj.toString();
				}
			}
		}
		return propertyValue;
	}


	void addProperty(final Vlayout parent, final String propertyTitle, final String propertyContent)
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
