# WellnessBuddy Home Screen Widget

## Overview
The WellnessBuddy widget displays your daily progress directly on your Android home screen, including:
- **Habits Progress**: Shows completed vs total habits with percentage
- **Mood Status**: Displays your latest mood emoji and count of today's mood entries
- **Hydration Progress**: Shows current water intake vs daily target with percentage
- **Date**: Shows current date

## Features
- **Real-time Updates**: Widget automatically updates when you log habits, moods, or water intake
- **Tap to Open**: Tap the widget to open the WellnessBuddy app
- **Responsive Design**: Widget adapts to different screen sizes
- **Auto-refresh**: Updates every 30 minutes automatically

## How to Add the Widget

### Step 1: Add Widget to Home Screen
1. Long-press on an empty area of your home screen
2. Select "Widgets" or "Add Widget"
3. Find "Wellness Buddy" in the widget list
4. Drag the "Wellness Progress" widget to your desired location
5. Resize if needed by dragging the edges

### Step 2: Widget Size Options
- **Minimum Size**: 4x2 grid cells (250x110dp)
- **Maximum Size**: Up to 400x200dp
- **Resizable**: Yes, horizontally and vertically

## Widget Layout
```
┌─────────────────────────────────┐
│ ❤️  Wellness Progress    Dec 15 │
│                                 │
│ 📋 Habits              2/5 (40%)│
│ ████████░░░░░░░░░░░░░░░░░░░░░░  │
│                                 │
│ 😊 Mood                 (3)     │
│                                 │
│ 💧 Water         1200/2000ml 60%│
│ ████████████░░░░░░░░░░░░░░░░░░  │
│                                 │
│        Tap to open app          │
└─────────────────────────────────┘
```

## Data Sources
The widget reads data from the same SharedPreferences storage used by the main app:
- **Habits**: From `PrefsStore.getHabits()`
- **Mood Entries**: From `PrefsStore.getMoods()`
- **Water Intake**: From `PrefsStore.getCurrentWaterIntake()`
- **Water Target**: From `PrefsStore.getDailyWaterTargetMl()`

## Automatic Updates
The widget refreshes automatically when:
- You add/complete a habit
- You log a new mood entry
- You add water intake
- Every 30 minutes (system update)

## Technical Details

### Files Created:
- `WellnessWidgetProvider.kt` - Main widget logic
- `widget_wellness_layout.xml` - Widget UI layout
- `wellness_widget_info.xml` - Widget configuration
- `widget_background.xml` - Widget background gradient
- `progress_background.xml` - Progress bar background
- `progress_fill.xml` - Progress bar fill
- `ic_wellness.xml` - Widget icon

### Widget Provider Features:
- Handles widget updates and refresh events
- Calculates daily progress percentages
- Manages click intents to open main app
- Supports multiple widget instances

### Integration:
- ViewModels automatically refresh widget when data changes
- Widget reads data directly from SharedPreferences
- No additional permissions required

## Troubleshooting

### Widget Not Updating:
1. Check if the app has been granted necessary permissions
2. Try removing and re-adding the widget
3. Restart the device if updates are still not working

### Widget Too Small/Large:
1. Long-press the widget to enter resize mode
2. Drag the edges to adjust size
3. Widget supports both horizontal and vertical resizing

### Data Not Showing:
1. Ensure you have logged some data in the main app first
2. Check that the app is not in battery optimization mode
3. Verify that the widget has permission to access app data

## Future Enhancements
Potential improvements for future versions:
- Multiple widget sizes (1x1, 2x1, 4x2, 4x4)
- Widget configuration options
- Different widget themes
- Quick actions (add water, log mood directly from widget)
- Weekly/monthly progress views
