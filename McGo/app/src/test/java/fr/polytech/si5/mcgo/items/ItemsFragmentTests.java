package fr.polytech.si5.mcgo.items;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemsFragmentTests {

    @Mock
    private ItemsContract.View mItemsView;

    private ItemsPresenter mItemsPresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mItemsPresenter = new ItemsPresenter(mItemsView);

        // The presenter won't update the view unless it's active.
        when(mItemsView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mItemsPresenter = new ItemsPresenter(mItemsView);

        // Then the presenter is set to the view
        verify(mItemsView).setPresenter(mItemsPresenter);
    }

}
