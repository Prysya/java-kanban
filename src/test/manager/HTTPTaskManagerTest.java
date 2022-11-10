package test.manager;

import main.manager.HTTPTaskManager;
import main.utils.Managers;

public class HTTPTaskManagerTest extends FileBackedTasksManagerTest {
    @Override
    HTTPTaskManager createTaskManager() {
        return Managers.getDefault();
    }
}
