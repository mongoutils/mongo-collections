
class mongodb {

	Exec { path => ["/bin/", "/sbin/", "/usr/bin/", "/usr/sbin/", "/usr/local/bin"], user => root, }
	File { owner => 0, group => 0, mode => 0644 }

	exec {"apt-get_update":
		command => "apt-get update",
	} ->

	package { "mongodb":
		ensure => present,
	} ->

	file { "/etc/mongodb.conf":
		source => "puppet:///modules/mongodb/mongodb.conf",
		notify => Service[ "mongodb" ],
	} ->

	service { "mongodb":
		ensure => running,
		require => File["/etc/mongodb.conf"],
	}

}
