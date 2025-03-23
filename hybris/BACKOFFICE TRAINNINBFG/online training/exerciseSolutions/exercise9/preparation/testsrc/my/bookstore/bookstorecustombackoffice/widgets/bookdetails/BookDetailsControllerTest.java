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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Labels;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.impl.DefaultObjectPreviewService;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvents;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.DeclaredInputs;
import com.hybris.cockpitng.testing.exception.WidgetTestException;

import my.bookstore.bookstorecustombackoffice.config.details.jaxb.Details;
import my.bookstore.bookstorecustombackoffice.config.details.jaxb.Property;
import my.bookstore.core.model.BookModel;


/**
 * Contains all the unit tests for {@link BookDetailsController} .
 *
 * @author SAP Hybris Education
 *
 */
@UnitTest
// TODO exercise 9.2: declare the inputs of the Book Details widget for the test class!
@DeclaredGlobalCockpitEvents(value =
{ @DeclaredGlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT),
		@DeclaredGlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT) })
public class BookDetailsControllerTest extends AbstractWidgetUnitTest<BookDetailsController>
{
	private static String TEST_IMAGE_URL = "http://the.image.url";

	@InjectMocks
	private final BookDetailsController bookDetailsController = Mockito.spy(new BookDetailsController());

	@Spy
	private final Div imgDiv = new Div();
	@Spy
	private final Div infoDiv = new Div();
	@Mock
	private DefaultObjectPreviewService objectPreviewService;
	@Mock
	private Popup bookImagePopup;

	// The following three are test/mock variables used by the tests
	private BookModel testBook = null;
	private Base baseConfig;
	private Details detailsConfig;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BookDetailsController getWidgetController()
	{
		return bookDetailsController;
	}

	/**
	 * Checks if a the controller behaves properly, when a book is sent through its socket.
	 */
	@Ignore
	@Test
	public void testReceiveBookBySocketEvent()
	{
		// TODO exercise 9.6
		// simulates the socket event

		// checks whether populateImgDiv was called

		// checks whether populateInfoDiv was called

		// checks whether addProperty method was called three times as a result of sending a book through the input socket
	}

	/**
	 * Prepares the mocks and test data to be used by the test methods. This method runs before any other test is run.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		prepareDetailsConfiguration();
		prepareBaseConfiguration();
		prepareBookInstance();

		Mockito.doReturn(detailsConfig).when(getWidgetController()).loadConfiguration(BookModel._TYPECODE,
				BookDetailsController.WIDGET_SETTING_PROPERTIES_CONFIG_CONTEXT, Details.class);
		Mockito.doReturn(baseConfig).when(getWidgetController()).loadConfiguration(BookModel._TYPECODE,
				BookDetailsController.WIDGET_SETTING_CONFIG_CONTEXT, Base.class);

		Mockito.doReturn("Property Value").when(getWidgetController()).getPropertyValue(Mockito.anyString(), Mockito.eq(testBook));

		final List<Component> popupChildren = Mockito.mock(ArrayList.class);
		Mockito.when(bookImagePopup.getChildren()).thenReturn(popupChildren);

		final ObjectPreview testPreview = Mockito.spy(new ObjectPreview(TEST_IMAGE_URL, BookDetailsController.IMAGE_MIME, true));
		Mockito.when(objectPreviewService.getPreview(Mockito.any(BookModel.class), Mockito.any(Base.class)))
				.thenReturn(testPreview);
		Mockito.when(objectPreviewService.getPreview(BookDetailsController.IMAGE_MIME)).thenReturn(testPreview);
		Mockito.when(testPreview.getUrl()).thenReturn(TEST_IMAGE_URL);
	}

	/**
	 * Prepares an instance of type {@link BookModel} that can be used by the tests within the class. It assigns this
	 * instance to {@code this.testBook} .
	 *
	 */
	private void prepareBookInstance()
	{
		if (testBook != null)
		{
			return;
		}
		testBook = Mockito.mock(BookModel.class);
		Mockito.when(testBook.getName()).thenReturn("Test Book");
		final UserModel testAuthor = new UserModel();
		testAuthor.setName("Test Author");
		final List testAuthors = new ArrayList<UserModel>(Arrays.asList(testAuthor));
		Mockito.when(testBook.getAuthors()).thenReturn(testAuthors);
		Mockito.when(testBook.getISBN10()).thenReturn("1234123412");
		Mockito.when(testBook.getISBN13()).thenReturn("1234123412341");
		Mockito.when(testBook.getDescription()).thenReturn("This book is only for testing purposes.");
	}

	/**
	 * Prepares an instance of the {@link Details} configuration type that can be used by the tests within the class. It
	 * assigns this instance to {@code this.detailsConfig} .
	 *
	 */
	private void prepareDetailsConfiguration()
	{
		if (this.detailsConfig != null)
		{
			return;
		}
		final Details configuration = new Details();
		final Property prop1 = new Property();
		prop1.setLabel("TestTitle");
		prop1.setName("name");
		final Property prop2 = new Property();
		prop2.setLabel("TestAuthors");
		prop2.setName("authors");
		final Property prop3 = new Property();
		prop3.setLabel("TestISBN10");
		prop3.setName("ISBN10");
		configuration.getProperty().clear();
		configuration.getProperty().add(prop1);
		configuration.getProperty().add(prop2);
		configuration.getProperty().add(prop3);
		this.detailsConfig = configuration;
	}

	/**
	 * Prepares an instance of the {@link Base} configuration type that can be used by the tests within the class. It
	 * assigns this instance to {@code this.detailsConfig} .
	 *
	 */
	private void prepareBaseConfiguration()
	{
		if (this.baseConfig != null)
		{
			return;
		}
		final Base configuration = new Base();
		final Labels l = new Labels();
		l.setLabel("'Title: \"' + (name?:code) + '\" - ' + @labelService.getObjectLabel(catalogVersion)");
		l.setIconPath("thumbnail?:picture");
		configuration.setLabels(l);
		this.baseConfig = configuration;
	}

	/**
	 * A wrapper for {@link com.hybris.cockpitng.testing.AbstractWidgetUnitTest#testNullSafeSocketInputs()} to avoid failing
	 * the test because of an inadequate context missing a LocaleProvider.
	 *
	 */
	@Override
	public void testNullSafeSocketInputs()
	{
		try
		{
			super.testNullSafeSocketInputs();
		}
		catch (final WidgetTestException ex)
		{
			// it could be done in a better way by using the cause of the exception, but unfortunately
			// WidgetTestException doesn't include it's {@link Throwable} Cause
			if (!(ex.getMessage().contains("java.lang.IllegalStateException: there is no LocaleProvider")
					&& ex.getMessage().contains("1 errors")))
			{
				throw ex;
			}
		}
	}
}
