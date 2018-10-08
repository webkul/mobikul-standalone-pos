var gulp         =  require('gulp');
var less         =  require('gulp-less');


/* Task to compile less */
gulp.task( 'compile-less', function() {
  gulp.src( '../less/style.less' )
    .pipe( less() )
    .pipe(gulp.dest( '../css/' ) );
});



/* Task to watch less changes */
gulp.task('watch-less', function() {
  gulp.watch( '../less/**/*.less' , ['compile-less']);
});

/* Task when running `gulp` from terminal */
gulp.task('default', ['watch-less']);
