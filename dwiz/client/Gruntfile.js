var _ = require('lodash');

module.exports = function (grunt) {

  function browserify(extraOptions) {
    var options = {
      browserifyOptions: {
        builtins: [ 'fs' ],
        commondir: false,
        detectGlobals: false,
        insertGlobalVars: []
      },
      transform: [ 'brfs' ]
    };

    var config = {
      options: _.merge(options, extraOptions),
      files: {
        '<%= config.dist %>/app.js': [ '<%= config.sources %>/app.js' ]
      }
    };

    return config;
  }

  /* global process*/

  // configures browsers to run test against
  // any of [ 'PhantomJS', 'Chrome', 'Firefox', 'IE']
  var TEST_BROWSERS = ((process.env.TEST_BROWSERS || '').replace(/^\s+|\s+$/, '') || 'PhantomJS').split(/\s*,\s*/g);

  require('time-grunt')(grunt);
  require('load-grunt-tasks')(grunt);

  grunt.loadNpmTasks('grunt-contrib-less');

  grunt.initConfig({

    pkg: grunt.file.readJSON('package.json'),

    config: {
      sources: 'src/js',
      less: 'src/less',
      dist: '../app/target/classes/web',
      assets: 'assets',
      tests: 'test',
      bower_components: 'bower_components'
    },

    jshint: {
      src: [ '<%= config.sources %>' ],
      gruntfile: [ 'Gruntfile.js' ],
      options: {
        jshintrc: true
      }
    },

    less: {
      dist: {
        options: {
          paths: [
            "<%= config.less %>",
            "<%= config.bower_components %>"
          ],
          cleancss: true
        },
        files: {
          "<%= config.dist %>/css/kickr.css": "<%= config.less %>/kickr.less"
        }
      }
    },

    karma: {
      options: {
        configFile: '<%= config.tests %>/config/karma.unit.js'
      },
      single: {
        singleRun: true,
        autoWatch: false,

        browsers: TEST_BROWSERS,

        browserify: {
          debug: false,
          transform: [ 'brfs' ]
        }
      },
      unit: {
        browsers: TEST_BROWSERS
      }
    },
    browserify: {
      app: browserify({}),
      watch: browserify({ watch: true })
    },
    uglify: {
      dist: {
        options: {
          sourceMap: true,
          sourceMapIncludeSources: true
        },
        files: {
          '<%= config.dist %>/app.js': [ '<%= config.dist %>/app.js' ]
        }
      }
    },
    copy: {
      resources: {
        files: [
          // index.html
          {
            expand: true,
            cwd: '<%= config.sources %>',
            src: [ '*.html' ],
            dest: '<%= config.dist %>'
          },

          // assets
          {
            expand: true,
            cwd: '<%= config.assets %>',
            src: [
              '**/*'
            ],
            dest: '<%= config.dist %>'
          },
          // bower dist folders
          {
            expand: true,
            cwd: '<%= config.bower_components %>/bootstrap/dist',
            src: [
              'fonts/**'
            ],
            dest: '<%= config.dist %>'
          }
        ]
      }
    },

    watch: {
      resources: {
        files: [
          '<%= config.assets %>/img/**/*',
          '<%= config.assets %>/css/**/*',
          '<%= config.sources %>/*.html'
        ],
        tasks: [ 'copy:resources' ]
      },
      less : {
        files: [
          '<%= config.less %>/*.less'
        ],
        tasks: [ 'less' ]
      }
    }
  });

  grunt.registerTask('test', 'karma:single');
  grunt.registerTask('build', [ 'less', 'browserify:app', 'copy' ]);
  grunt.registerTask('auto-build', [
    'build',
    'browserify:watch',
    'watch'
  ]);

  grunt.registerTask('default', [ 'jshint', 'test', 'build', 'uglify' ]);
};
