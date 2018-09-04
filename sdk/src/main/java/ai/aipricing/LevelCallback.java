package ai.aipricing;

import android.support.annotation.WorkerThread;

/**
 * An interface representing a callback to be notified about the results of
 */

public interface LevelCallback {
    /**
     * Success callback method.
     * @param level get level.
     */
    @WorkerThread
    void onSuccess(int level);
}