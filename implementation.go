package main

import (
	"fmt"
	"sync"
	"time"
)

type Task struct {
	Name string
}

func main() {
	var wg sync.WaitGroup
	taskQueue := make(chan Task, 10)
	results := []string{}

	// Simulate adding tasks
	for i := 0; i < 10; i++ {
		taskQueue <- Task{Name: fmt.Sprintf("Task %d", i)}
	}

	// Worker goroutines
	for i := 0; i < 4; i++ {
		wg.Add(1)
		go worker(taskQueue, &results, &wg)
	}

	// Close channel after all tasks are added
	close(taskQueue)

	// Wait for all goroutines to finish
	wg.Wait()

	// Print results
	for _, result := range results {
		fmt.Println(result)
	}
}

func worker(taskQueue chan Task, results *[]string, wg *sync.WaitGroup) {
	defer wg.Done()
	for task := range taskQueue {
		// Simulate task processing
		time.Sleep(1 * time.Second)
		*results = append(*results, "Processed: "+task.Name)
	}
}
